package com.pam.gps.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.pam.gps.R
import com.pam.gps.extensions.validate
import com.pam.gps.extensions.validateEmail
import com.pam.gps.extensions.withValidateEmail
import com.pam.gps.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

class LoginFragment : Fragment() {

  private val viewModel by viewModels<LoginViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_login, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.authStatus.observe(
      viewLifecycleOwner, Observer {
        findNavController().navigate(R.id.action_navigation_login_to_navigation_home)
      })
    login_button.setOnClickListener {
      if (!username_text.validateEmail()) {
        login_layout.error = resources.getString(R.string.email_error)
        username_text.requestFocus()
        return@setOnClickListener
      }
      login_layout.error = null
      if (!password_text.validate(String::isNotEmpty)) {
        password_layout.error = resources.getString(R.string.password_error_empty)
        password_text.requestFocus()
        return@setOnClickListener
      }
      password_layout.error = null

      hideKeyboard(requireContext(), view)
      val handler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
          is FirebaseAuthInvalidUserException -> {
            Timber.e(throwable)
          }
          is FirebaseAuthInvalidCredentialsException -> {
            password_layout.error = resources.getString(R.string.password_error_match)
            Timber.e(throwable)
          }
          else -> Timber.e(throwable)
        }
      }
      viewModel.authenticate(
        username_text.text.toString(),
        password_text.text.toString(),
        handler
      )
    }
    username_text.withValidateEmail(
      login_layout,
      resources.getString(R.string.email_error)
    )
  }
}
