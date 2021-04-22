package com.sanjesh.motomart.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sanjesh.motomart.API.Statccart
import com.sanjesh.motomart.Adapter.CartAdapter
import com.sanjesh.motomart.Interface.CartRefresh
import com.sanjesh.motomart.MainActivity
import com.sanjesh.motomart.R
import com.sanjesh.motomart.Repository.CartRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class CategoryFragment : Fragment(),View.OnClickListener, CartRefresh {
    private lateinit var tvCartDesc : TextView
    private lateinit var btnContinue : Button
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var btnDelete: Button
    private lateinit var checkout: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        tvCartDesc = view.findViewById(R.id.tvCartDesc)
        btnContinue = view.findViewById(R.id.btnContinue)
        recycler = view.findViewById(R.id.recycler)
        btnDelete = view.findViewById(R.id.btnDelete)
        checkout = view.findViewById(R.id.checkout)
        initialize()
        btnDelete.setOnClickListener(this)
        btnContinue.setOnClickListener(this)
        return view
    }
    private fun initialize()
    {


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cartRepository = CartRepo()
                val response = cartRepository.retrieveCart()
                if (response.success == true) {
                    Statccart.lstCart = response.data!!
                    println(response)
                    withContext(Dispatchers.Main)
                    {
                        loadData()
                    }

                }
                else
                {

                    withContext(Dispatchers.Main)
                    {
                        val snk = Snackbar.make(recycler,"Not found", Snackbar.LENGTH_LONG)
                        snk.setAction("Cancel",View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                    }
                }
            }
            catch (ex:Exception)
            {
                withContext(Dispatchers.Main)
                {
                    val snk = Snackbar.make(recycler,"${ex.toString()}", Snackbar.LENGTH_LONG)
                    snk.setAction("Cancel",View.OnClickListener {
                        snk.dismiss()
                    })
                    snk.show()
                    println(ex.printStackTrace())
                }
            }
        }

    }

    private fun loadData()
    {
        if(Statccart.lstCart.size > 0)
        {
            tvCartDesc.text = "${Statccart.lstCart.size} items in cart."
            adapter = CartAdapter(requireContext(),Statccart.lstCart,this)
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireContext())
            btnContinue.visibility = View.GONE
        }
        else
        {
            tvCartDesc.text = "No items in cart."
            btnContinue.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnDelete->{

                if(Statccart.myCart.size>0)
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = CartRepo()
                            val response = repo.deleteCart(Statccart.myCart[0]._id)
                            if(response.success == true)
                            {
                                Statccart.lstCart.remove(Statccart.lstCart[0])

                                withContext(Dispatchers.Main)
                                {
                                    loadContent()
                                }
                            }
                            else
                            {
                                withContext(Dispatchers.Main)
                                {
                                    val snk = Snackbar.make(recycler,"${response.message}", Snackbar.LENGTH_LONG)
                                    snk.setAction("Ok",View.OnClickListener {
                                        snk.dismiss()
                                    })
                                    snk.show()
                                }
                            }
                        }
                        catch (ex:Exception)
                        {
                            withContext(Dispatchers.Main)
                            {
                                val snk = Snackbar.make(recycler,"${ex.printStackTrace()}", Snackbar.LENGTH_LONG)
                                snk.setAction("Ok",View.OnClickListener {
                                    snk.dismiss()
                                })
                                println(ex.printStackTrace())
                                snk.show()
                            }
                        }
                    }
                }


            }

            R.id.btnContinue->{
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun refreshCartActivity() {
        if (Statccart.myCart.size > 0) {

            var prices = Statccart.myCart.map {
                it.pprice
            }
            var totalPrice = prices.reduce { acc, i ->
                acc + i
            }

            btnDelete.visibility = View.VISIBLE
            checkout.visibility = View.VISIBLE
            checkout.text = "Total Checkout: Rs ${totalPrice}"
        } else {
            btnDelete.visibility = View.GONE
            checkout.visibility = View.GONE
            checkout.text = "Total Checkout: Rs ${0}"

        }
    }
    private fun loadContent()
    {
        adapter = CartAdapter(requireContext(),Statccart.lstCart,this)
        recycler.adapter = adapter
        adapter.notifyDataSetChanged()
        Statccart.myCart = mutableListOf()
        btnDelete.visibility=View.GONE
        checkout.visibility = View.GONE
        if(Statccart.lstCart.size > 0)
        {
            tvCartDesc.text = "${Statccart.lstCart.size} items in cart."
            btnContinue.visibility = View.GONE
        }
        else
        {
            tvCartDesc.text = "No Items in cart."
            btnContinue.visibility = View.VISIBLE
        }
    }

}

