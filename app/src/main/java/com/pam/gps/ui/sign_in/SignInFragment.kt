package com.pam.gps.ui.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.pam.gps.R
import com.pam.gps.extensions.setOnClickListenerRequirements
import com.pam.gps.extensions.validateEmail
import com.pam.gps.extensions.withValidate
import com.pam.gps.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

class SignInFragment : Fragment() {

  private val viewModel by viewModels<SignInViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_sign_in, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val validateEmail: () -> Boolean = { email_text.text.toString().validateEmail() }
    val validatePassword: () -> Boolean = { password_text.text.toString().isNotEmpty() }
    val emailErrorText = resources.getString(R.string.email_error)
    val passwordErrorText = resources.getString(R.string.password_error_empty)

    viewModel.authStatus.observe(
      viewLifecycleOwner, Observer {
        findNavController().navigate(R.id.action_navigation_login_to_navigation_home)
      })
    viewModel.loading.observe(
      viewLifecycleOwner, Observer { loading ->
        progress_bar.visibility = if (loading) View.VISIBLE else View.INVISIBLE
        sign_in_button.isEnabled = !loading
        forgot_password_button.isEnabled = !loading
      }
    )

    email_text.withValidate(email_layout, emailErrorText, validateEmail)
    email_text.addTextChangedListener { email_layout.error = null }

    password_text.withValidate(password_layout, passwordErrorText, validatePassword)
    password_text.addTextChangedListener { password_layout.error = null }

    sign_in_button.setOnClickListenerRequirements(
      Pair(email_text, validateEmail),
      Pair(password_text, validatePassword),
      listener = View.OnClickListener {
        hideKeyboard(requireContext(), view)
        sign_in_layout.requestFocus()
        viewModel.authenticate(
          email_text.text.toString(),
          password_text.text.toString(),
          signInExceptionHandler()
        )
      }
    )
  }

  private fun signInExceptionHandler(): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, throwable ->
      if (throwable is FirebaseAuthInvalidCredentialsException) {
        password_layout.error = resources.getString(R.string.password_error_match)
      } else {
        Toast.makeText(context, R.string.unknown_auth_error, Toast.LENGTH_LONG).show()
        Timber.e(throwable)
      }
    }
  }
}
