package com.pam.gps.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pam.gps.R
import com.pam.gps.extensions.clicks
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class LoginFragment : Fragment() {

  private val viewModel by viewModels<LoginViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_login, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    //TODO Replace with DataBinding
    lifecycleScope.launch {
      login_button.clicks().collect {
        try {
          viewModel.authenticate(
            username_text.text.toString(),
            password_text.text.toString()
          ).await()
        } catch (e: Exception) {
          //TODO Error Handling
          Timber.e(e.localizedMessage)
        }
      }
    }

    viewModel.authStatus.observe(
      viewLifecycleOwner,
      Observer { if (it == LoginViewModel.AuthStatus.AUTHENTICATED) findNavController().popBackStack() })
  }

}
