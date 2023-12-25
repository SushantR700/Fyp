package com.example.foodgasm.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodgasm.R
import com.example.foodgasm.activities.ShoppingActivity
import com.example.foodgasm.databinding.FragmentLoginBinding
import com.example.foodgasm.dialog.setupBottomSheetDialog
import com.example.foodgasm.utils.Resource
import com.example.foodgasm.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment:Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("MY_PRE",Context.MODE_PRIVATE)
        val getUsername = sharedPreferences.getString("EMAIL","")
        val getPassword = sharedPreferences.getString("PASSWORD","")

        if(getUsername != "" && getPassword != ""){
            Intent(requireContext(),ShoppingActivity::class.java).also {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
            }
        }



        binding.textView2.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        binding.apply {
            register.setOnClickListener{
                val email=editText.text.toString().trim()
                val password=editText2.text.toString()
                if(email!="" && password!=""){
                viewModel.login(email,password)
            }}

        }
        binding.forgotpassword.setOnClickListener {
            setupBottomSheetDialog {
                email-> viewModel.resetPassword(email)

            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect{
                when(it){
                    is Resource.Loading -> {}

                    is Resource.Success ->{

                        Snackbar.make(requireView(),"Link has been sent to the email address",Snackbar.LENGTH_LONG).show()
                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(),"Failed:${it.message.toString()}",Snackbar.LENGTH_LONG).show()
                    }



                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.register.startAnimation()
                    }

                    is Resource.Success ->{
                        val email = binding.editText.text.toString()
                        val password = binding.editText2.text.toString()

                        val sharedPreferences = requireActivity().getSharedPreferences("MY_PRE",Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("EMAIL",email);
                        editor.putString("PASSWORD",password)
                        editor.apply()
                        binding.register.revertAnimation()
                        Intent(requireContext(),ShoppingActivity::class.java).also {
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(it)
                        }

                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        binding.register.revertAnimation()
                    }

                    is Resource.Verify ->{
                        Toast.makeText(requireContext(),"Please verify your email",Toast.LENGTH_LONG).show()
                        binding.register.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

    }
}