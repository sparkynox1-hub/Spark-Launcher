package com.sparkynox.sparklauncher.data.repository

import android.app.Activity
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalException
import com.sparkynox.sparklauncher.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AuthRepository — handles Microsoft account login via MSAL.
 * Cracked login is handled directly in AuthViewModel (no repo needed).
 * Author: SparkyNox
 */
@Singleton
class AuthRepository @Inject constructor() {

    sealed class MsAuthResult {
        data class Success(
            val username: String,
            val uuid: String,
            val accessToken: String,
            val refreshToken: String
        ) : MsAuthResult()
        data class Error(val message: String) : MsAuthResult()
    }

    private var msalClient: IPublicClientApplication? = null

    fun loginMicrosoft(activity: Activity, callback: (MsAuthResult) -> Unit) {
        PublicClientApplication.createSingleAccountPublicClientApplication(
            activity,
            R.raw.msal_config,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    msalClient = application
                    acquireToken(activity, application, callback)
                }

                override fun onError(exception: MsalException) {
                    callback(MsAuthResult.Error(exception.message ?: "MSAL init failed"))
                }
            }
        )
    }

    private fun acquireToken(
        activity: Activity,
        app: ISingleAccountPublicClientApplication,
        callback: (MsAuthResult) -> Unit
    ) {
        val scopes = arrayOf("XboxLive.SignIn", "XboxLive.offline_access")

        app.signIn(
            activity,
            null,
            scopes,
            object : AuthenticationCallback {
                override fun onSuccess(result: IAuthenticationResult) {
                    // Exchange MS token for Xbox token then Minecraft token
                    // This is a simplified flow — full impl needs Xbox auth chain
                    callback(
                        MsAuthResult.Success(
                            username = result.account.username ?: "Player",
                            uuid = result.account.id ?: "",
                            accessToken = result.accessToken,
                            refreshToken = ""  // MSAL handles refresh internally
                        )
                    )
                }

                override fun onError(exception: MsalException) {
                    callback(MsAuthResult.Error(exception.message ?: "Login failed"))
                }

                override fun onCancel() {
                    callback(MsAuthResult.Error("Login cancelled"))
                }
            }
        )
    }
}
