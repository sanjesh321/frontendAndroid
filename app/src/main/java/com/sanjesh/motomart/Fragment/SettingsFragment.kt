package com.sanjesh.motomart.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sanjesh.motomart.LogInAct
import com.sanjesh.motomart.ProductView
import com.sanjesh.motomart.R
import com.sanjesh.motomart.Update

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private lateinit var btn: TextView
    private lateinit var Cart: TextView
    private lateinit var Logout: TextView
    private lateinit var aboutUs: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        btn = view.findViewById(R.id.btn)
        Cart = view.findViewById(R.id.Cart)
        Logout = view.findViewById(R.id.Logout)
        aboutUs = view.findViewById(R.id.aboutUs)
        btn.setOnClickListener{
            startActivity(Intent(requireContext(),Update::class.java))
        }
//        Cart.setOnClickListener {
//        }
        aboutUs.setOnClickListener {
            startActivity(Intent(requireContext(), ProductView::class.java))
        }

        Logout.setOnClickListener{

            logout()
        }

        return view
    }

    fun logout()
    {
        var pref = context!!.getSharedPreferences("credentials", Context.MODE_PRIVATE)
        var editor = pref.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(context, LogInAct::class.java)
        context!!.startActivity(intent)
    }
}