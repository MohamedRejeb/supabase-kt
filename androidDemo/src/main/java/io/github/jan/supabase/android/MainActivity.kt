package io.github.jan.supabase.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import io.github.jan.supabase.auth.GoTrue
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.auth.host
import io.github.jan.supabase.auth.providers.Discord
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.scheme
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://arnyfaeuskyqfxkvotgj.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFybnlmYWV1c2t5cWZ4a3ZvdGdqIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NTMwMzkxMTEsImV4cCI6MTk2ODYxNTExMX0.ItmL8lfnOL9oy7CEX9N6TnYt10VVhk-KTlwley4aq1M"
    ) {
        install(GoTrue) {
            scheme = "supabase"
            host = "login"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supabaseClient.handleDeeplinks(intent)
        setContent {
            MaterialTheme {
                val session by supabaseClient.auth.currentSession.collectAsState()
                val scope = rememberCoroutineScope()
                if (session != null) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Logged in as ${session?.user?.email}")
                    }
                } else {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        var email by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }
                        Column {
                            TextField(email, { email = it }, placeholder = { Text("Email") })
                            TextField(
                                password,
                                { password = it },
                                placeholder = { Text("Password") },
                                visualTransformation = PasswordVisualTransformation()
                            )
                            Button(onClick = {
                                scope.launch {
                                    supabaseClient.auth.loginWith(Email) {
                                        this.email = email
                                        this.password = password
                                    }
                                }
                            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                Text("Login")
                            }
                            Button(onClick = {
                                scope.launch {
                                    supabaseClient.auth.loginWith(Discord)
                                }
                            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                Text("Login with Discord")
                            }
                            //
                        }
                    }
                }
            }
        }
    }

}