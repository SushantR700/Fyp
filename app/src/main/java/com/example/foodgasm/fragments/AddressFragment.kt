package com.example.foodgasm.fragments

import com.example.foodgasm.data.Address
import com.example.foodgasm.databinding.FragmentAddressBinding
import com.example.foodgasm.utils.Resource
import com.example.foodgasm.viewmodel.AddressViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment : Fragment() {
    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()
    val args by navArgs<AddressFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        findNavController().navigateUp()
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address = args.address
        if (address == null) {
            binding.buttonDelelte.visibility = View.GONE
        } else {
            binding.apply {
                edAddressTitle.setText(address.addressTitle)
                edFullName.setText(address.fullName)
                edState.setText(address.street)
                edPhone.setText(address.phone)
                edCity.setText(address.city)
                edState.setText(address.state)
            }
        }

        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()
                val address = Address(addressTitle, fullName, street, phone, city, state)

                viewModel.addAddress(address)
            }
        }


    }
    override fun onResume() {
        super.onResume()
        // Add back button listener
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Navigate up when back button is pressed
            findNavController().navigateUp()
        }
    }

}