package com.victorlaerte.wedeploywebinar

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wedeploy.android.Callback
import com.wedeploy.android.WeDeploy
import com.wedeploy.android.transport.Response
import java.lang.Exception

/**
 * @author Victor Oliveira
 */
class ItemAdapter(var items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)

        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val currentItem = items[position]
        holder.name.text = currentItem.name
        holder.description.text = currentItem.description

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, AddItemActivity::class.java).apply {
                putExtra("item", currentItem)
            }

            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context

            AlertDialog.Builder(context)
                .setTitle("Question")
                .setMessage("Do you really want to remove this item?")
                .setPositiveButton("YES", { dialog, which ->

                    val weDeploy = WeDeploy.Builder().build()

                    weDeploy.data(MainActivity.URL)
                        .delete("items/${currentItem.id}")
                        .execute(object: Callback {

                            override fun onSuccess(response: Response?) {
                                response?.let {
                                    Log.d("teste", "${it.statusCode} ${it.statusMessage} ${it.body}")

                                }
                            }

                            override fun onFailure(e: Exception?) {
                                e?.let {
                                    Log.d("teste", e.message)
                                }
                            }
                        })


                })
                .setNegativeButton("NO", { _, _ ->  })

                .show()

            true
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.nameTextView)
        var description: TextView = itemView.findViewById(R.id.descriptionTextView)
    }
}