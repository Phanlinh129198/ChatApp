package com.rikkei.tranning.chatapp.views.uis.signup

import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.rikkei.tranning.chatapp.R
import com.rikkei.tranning.chatapp.base.BaseFragment
import com.rikkei.tranning.chatapp.databinding.FragmentSignupBinding
import com.rikkei.tranning.chatapp.helper.LocaleHelper
import com.rikkei.tranning.chatapp.views.uis.login.LoginFragment
import kotlinx.android.synthetic.main.fragment_signup.*

class SignUpFragment : BaseFragment<FragmentSignupBinding, SignUpViewModel>() {
    override fun getBindingVariable(): Int {
        return BR.viewModels
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_signup
    }

    override fun getViewModel(): SignUpViewModel {
        return ViewModelProviders.of(requireActivity()).get<SignUpViewModel>(
            SignUpViewModel::class.java
        )

    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        eventEnableButton()
        mViewDataBinding.ButtonBackRegister.setOnClickListener { replaceFragment() }
        mViewDataBinding.ButtonMoveLogin.setOnClickListener { replaceFragment() }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (LocaleHelper.getLanguage(activity) == "vi") {
            val htmlContentCheckBox =
                "   " + " Tôi đồng ý với các<font color=\"#4356B4\"> chính sách</font> và <font color=\"#4356B4\"> điều khoản</font>"
            mViewDataBinding.textViewCheckbox.text = Html.fromHtml(
                htmlContentCheckBox
            )
        } else {
            val htmlContentCheckBox =
                "   " + " I agree with the <font color=\"#4356B4\"> policies</font> and <font color=\"#4356B4\"> terms</font>"
            mViewDataBinding.textViewCheckbox.text = Html.fromHtml(
                htmlContentCheckBox
            )
        }

        val textSpan = mViewDataBinding!!.ButtonMoveLogin.text
        var index = textSpan.indexOf('?')
        val spannable = SpannableString(textSpan)
        spannable.setSpan(
            ForegroundColorSpan(Color.BLUE),
            ++index,
            textSpan.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mViewDataBinding!!.ButtonMoveLogin.text = spannable
        mViewModel!!.signUpStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SignUpViewModel.SignUpStatus.Loading -> {
                    if (it.loading)
                        progress_circular_signUp.visibility = View.VISIBLE
                    else
                        progress_circular_signUp.visibility = View.GONE
                }
                is SignUpViewModel.SignUpStatus.IsOk -> {
                    progress_circular_signUp.visibility = View.GONE
                    Toast.makeText(context, "Signup success", Toast.LENGTH_SHORT).show()
                    replaceFragment()
                }
                is SignUpViewModel.SignUpStatus.Failure -> {
                    Toast.makeText(context, "Sign up fail", Toast.LENGTH_SHORT).show()
                    mViewDataBinding.editTextPassRegister.text = null
                    mViewDataBinding.editTextEmailRegister.text = null
                    mViewDataBinding.checkboxRegister.isChecked = false
                    progress_circular_signUp.visibility = View.GONE
                }
                is SignUpViewModel.SignUpStatus.ErrorPassAndEmail -> {
                    Toast.makeText(context, "Invalid Email Or Password!", Toast.LENGTH_SHORT).show()
                    mViewDataBinding.editTextPassRegister.text = null
                    mViewDataBinding.editTextEmailRegister.text = null
                    mViewDataBinding.checkboxRegister.isChecked = false
                    progress_circular_signUp.visibility = View.GONE
                }
            }
        })

        mViewModel?.userName?.observe(
            viewLifecycleOwner,
            Observer { s ->
                if (TextUtils.isEmpty(s)) {
                    mViewDataBinding!!.editTextNameRegister.error = "REQUIRED"
                }
            })
    }

    private fun eventEnableButton() {
        val editTexts = arrayOf(
            mViewDataBinding.editTextNameRegister,
            mViewDataBinding.editTextEmailRegister,
            mViewDataBinding.editTextPassRegister
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
        mViewDataBinding.checkboxRegister.setOnClickListener { setEnableButton() }
    }

    private fun replaceFragment() {
        mViewDataBinding.editTextPassRegister.text = null
        mViewDataBinding.editTextEmailRegister.text = null
        mViewDataBinding.checkboxRegister.isChecked = false
        mViewModel.resetStatus()
        val fragmentTransaction = parentFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        FirebaseAuth.getInstance().signOut()
        var fragmentLogin = parentFragmentManager.findFragmentByTag("fragmentLogin")
        if (fragmentLogin == null) {
            fragmentLogin = LoginFragment()
        }
        fragmentTransaction.replace(R.id.FrameLayout, fragmentLogin, "fragmentLogin")
        parentFragmentManager.popBackStack()
        fragmentTransaction.commit()
    }

    fun setEnableButton() {
        if (mViewDataBinding.checkboxRegister.isChecked
            && !TextUtils.isEmpty(
                mViewDataBinding.editTextNameRegister.text.toString()
            )
            && !TextUtils.isEmpty(
                mViewDataBinding.editTextEmailRegister.text.toString()
            )
            && !TextUtils.isEmpty(
                mViewDataBinding.editTextPassRegister.text.toString()
            )
        ) {
            mViewDataBinding.ButtonRegister.isEnabled = true
            mViewDataBinding.ButtonRegister.setBackgroundResource(R.drawable.custom_button_enable)
        } else {
            mViewDataBinding.ButtonRegister.isEnabled = false
            mViewDataBinding.ButtonRegister.setBackgroundResource(R.drawable.custom_button_login)
        }
    }
}