package com.linh.doan.views.uis.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.View

import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.linh.doan.R
import com.linh.doan.base.BaseFragment
import com.linh.doan.databinding.FragmentLoginBinding
import com.linh.doan.views.uis.MainActivity
import com.linh.doan.views.uis.signup.SignUpFragment
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment<FragmentLoginBinding?, LoginViewModel?>() {
    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun getViewModel(): LoginViewModel {
        return ViewModelProviders.of(requireActivity()).get<LoginViewModel>(
            LoginViewModel::class.java
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val textSpan = mViewDataBinding!!.ButtonMoveRegister.text
        var index = textSpan.indexOf('?')
        val spannable = SpannableString(textSpan)
        spannable.setSpan(
            ForegroundColorSpan(Color.BLUE),
            ++index,
            textSpan.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mViewDataBinding!!.ButtonMoveRegister.text = spannable
        eventEditText()
        mViewDataBinding!!.ButtonMoveRegister.setOnClickListener { replaceFragment() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel!!.loginStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LoginViewModel.LoginStatus.Loading -> {
                    progress_circular.visibility = View.VISIBLE
                }
                is LoginViewModel.LoginStatus.IsOk -> {
                    progress_circular.visibility = View.GONE
//                    Toast.makeText(context, "Login Success!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                is LoginViewModel.LoginStatus.Failure -> {
                    Toast.makeText(context, "Login Failed!", Toast.LENGTH_SHORT).show()
                    mViewDataBinding!!.editTextEmailLogin.text = null
                    mViewDataBinding!!.editTextPassLogin.text = null
                    progress_circular.visibility = View.GONE

                }
                is LoginViewModel.LoginStatus.ErrorPassAndEmail -> {
                    if (it.isError) {
                        Toast.makeText(
                            context,
                            "Invalid Email Or Password! Login",
                            Toast.LENGTH_SHORT
                        ).show()
                        mViewDataBinding!!.editTextEmailLogin.text = null
                        mViewDataBinding!!.editTextPassLogin.text = null
                        progress_circular.visibility = View.GONE
                    }
                }
                is LoginViewModel.LoginStatus.Register -> {
                    replaceFragment()
                }
            }
        })
    }

    private fun eventEditText() {
        val editTexts = arrayOf(
            mViewDataBinding!!.editTextPassLogin,
            mViewDataBinding!!.editTextEmailLogin
        )
        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    setEnableButton()
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    private fun replaceFragment() {
        mViewDataBinding!!.editTextEmailLogin.setText("")
        mViewDataBinding!!.editTextPassLogin.setText("")
        mViewModel?.resetStatus()

        val fragmentTransaction = parentFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        var fragmentSignUp = parentFragmentManager.findFragmentByTag("fragmentSignUp")
        if (fragmentSignUp == null) {
            fragmentSignUp = SignUpFragment()
        }
        fragmentTransaction.replace(R.id.FrameLayout, fragmentSignUp, "fragmentSignUp")
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun setEnableButton() {
        if (!TextUtils.isEmpty(mViewDataBinding!!.editTextEmailLogin.text.toString())
            && !TextUtils.isEmpty(
                mViewDataBinding!!.editTextPassLogin.text.toString()
            )
        ) {
            mViewDataBinding!!.ButtonLogin.isEnabled = true
            mViewDataBinding!!.ButtonLogin.setBackgroundResource(R.drawable.custom_button_enable)
        } else {
            mViewDataBinding!!.ButtonLogin.isEnabled = false
            mViewDataBinding!!.ButtonLogin.setBackgroundResource(R.drawable.custom_button_login)
        }
    }
}