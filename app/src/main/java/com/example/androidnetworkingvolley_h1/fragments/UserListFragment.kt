package com.example.androidnetworkingvolley_h1.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.androidnetworkingvolley_h1.R
import com.example.androidnetworkingvolley_h1.adapter.UserAdapter
import com.example.androidnetworkingvolley_h1.databinding.FragmentUserListBinding
import com.example.androidnetworkingvolley_h1.model.User
import com.example.androidnetworkingvolley_h1.util.Constantas
import com.example.androidnetworkingvolley_h1.util.NetworkUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class UserListFragment : Fragment(R.layout.fragment_user_list) {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val userAdapter by lazy { UserAdapter() }
    private lateinit var requestQueue: RequestQueue

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserListBinding.bind(view)
        allCode()
    }

    private fun allCode() {

        val networkUtils = NetworkUtils(requireContext())
        if (networkUtils.isNetworkConnected()) {
            requestQueue = Volley.newRequestQueue(requireContext())
            getAllUsers()
        } else {
            Toast.makeText(requireContext(), "No internet", Toast.LENGTH_SHORT).show()
        }

        binding.rv.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        userAdapter.onClick = {
            val bundle = bundleOf("id" to it)
            findNavController().navigate(R.id.action_userListFragment_to_detailFragment, bundle)
        }

    }

    private fun getAllUsers() {
        val jsonUsers = JsonArrayRequest(
            Request.Method.GET,
            Constantas.BASE_URL,
            null,
            {
                binding.progressBar.isVisible = false
                val type: Type = object : TypeToken<List<User>>() {}.type
                val userList = Gson().fromJson<List<User>>(it?.toString(), type)
                userAdapter.submitList(userList)
            },
            {
                binding.progressBar.isVisible = false
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonUsers)
    }

}