package com.unipi.mpsp21043.client.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.unipi.mpsp21043.client.R
import com.unipi.mpsp21043.client.databinding.ItemProductBinding
import com.unipi.mpsp21043.client.models.Product
import com.unipi.mpsp21043.client.utils.GlideLoader
import com.unipi.mpsp21043.client.utils.IntentUtils


/**
 * A adapter class for products list items.
 */
open class ProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<ProductsListAdapter.ProductsViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ProductsViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(
            ItemProductBinding.inflate(
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
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val model = list[position]
        var priceReduced = model.price

        holder.binding.apply {
            GlideLoader(context).loadProductPictureWide(
                model.iconUrl,
                imageViewPicture
            )
            textViewName.text = model.name
            if (model.sale != 0.0) {
                textViewPriceReduced.apply {
                    visibility = View.VISIBLE
                    foreground =
                        AppCompatResources.getDrawable(context, R.drawable.striking_red_text)
                    text = String.format(
                        context.getString(R.string.text_format_price),
                        context.getString(R.string.curr_eur),
                        model.price
                    )
                }
                priceReduced = model.price - (model.price * model.sale)
            }
            textViewPrice.apply {
                text = String.format(
                    context.getString(R.string.text_format_price),
                    context.getString(R.string.curr_eur),
                    priceReduced
                )
            }

            textViewWeight.text = String.format(
                context.getString(R.string.text_format_weight),
                model.weight,
                model.weightUnit
            )
        }
        // Click listener on list item click
        holder.itemView.setOnClickListener {
            IntentUtils().goToProductDetailsActivity(context, model.id)
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
    class ProductsViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)
}
