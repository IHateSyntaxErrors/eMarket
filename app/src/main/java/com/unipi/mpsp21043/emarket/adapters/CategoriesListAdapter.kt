package com.unipi.mpsp21043.emarket.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unipi.mpsp21043.emarket.databinding.ItemCategoryBinding
import com.unipi.mpsp21043.emarket.models.Category
import com.unipi.mpsp21043.emarket.utils.GlideLoader
import com.unipi.mpsp21043.emarket.utils.IntentUtils


/**
 * A adapter class for categories list items.
 */
open class CategoriesListAdapter(
    private val context: Context,
    private var list: ArrayList<Category>
) : RecyclerView.Adapter<CategoriesListAdapter.CategoriesViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ProductsViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val model = list[position]

        holder.binding.apply {
            GlideLoader(context).loadCategoryIcon(model.imgUrl, imgViewIcon)
            txtViewHeader.text = model.name
            txtViewDescription.text = model.description
        }
        holder.itemView.setOnClickListener {
            Log.d("", "test")
            IntentUtils().goToListProductsActivity(context, model.name)
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class CategoriesViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}