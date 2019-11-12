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
      }
    )

  }


  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    //TODO Error handling
    val handler = CoroutineExceptionHandler { _, throwable ->
      when (throwable) {
        is Exception -> Timber.e(throwable)
      }
    }
    login_button.setOnClickListener {
      viewModel.authenticate(
        username_text.text.toString(),
        password_text.text.toString(),
        handler
      )
    }
  }
}
