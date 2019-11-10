package com.pam.gps.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pam.gps.R
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class LoginFragment : Fragment() {

  private val viewModel by viewModels<LoginViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.login_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    //TODO Replace with DataBinding
    login_button.setOnClickListener {
      viewModel.authenticate(
        username_text.text.toString(),
        password_text.text.toString()
      )
    }

    viewModel.authStatus.observe(
      viewLifecycleOwner,
      Observer {if(it == LoginViewModel.AuthStatus.AUTHENTICATED) findNavController().popBackStack() })
  }

}
