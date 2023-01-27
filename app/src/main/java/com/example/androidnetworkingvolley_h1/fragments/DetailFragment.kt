package com.example.androidnetworkingvolley_h1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.androidnetworkingvolley_h1.R
import com.example.androidnetworkingvolley_h1.adapter.UserAdapter
import com.example.androidnetworkingvolley_h1.databinding.FragmentDetailBinding
import com.example.androidnetworkingvolley_h1.model.User
import com.example.androidnetworkingvolley_h1.util.Constantas
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val userAdapter by lazy { UserAdapter() }
    private lateinit var requestQueue: RequestQueue
    private var id: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)
        id = arguments?.getInt("id")
        loadUser(id!!)
    }

    private fun loadUser(id: Int) {
        val requestQueue = Volley.newRequestQueue(requireContext())
        val userRequest = JsonObjectRequest(
            Request.Method.GET,
            "${Constantas.BASE_URL}/$id",
            null,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {
                    binding.progressBar2.isVisible = false
                    binding.textView.isVisible = true
                    binding.textView2.isVisible = true
                    binding.textView4.isVisible = true
                    val type: Type = object : TypeToken<User>() {}.type
                    val user = Gson().fromJson<User>(response?.toString(), type)
                    binding.apply {
                        textView.text = user.id.toString()
                        textView2.text = user.name
                        textView4.text = user.email
                    }
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    binding.progressBar2.isVisible = false
                }
            }

        )
        requestQueue.add(userRequest)
    }


}