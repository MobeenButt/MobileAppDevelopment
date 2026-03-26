package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CartActivity : AppCompatActivity() {

    private lateinit var cartItemsContainer: LinearLayout
    private lateinit var subtotalText: TextView
    private lateinit var taxText: TextView
    private lateinit var shippingText: TextView
    private lateinit var totalText: TextView
    private lateinit var checkoutButton: Button
    private lateinit var clearCartButton: Button
    private lateinit var continueShoppingButton: Button
    private lateinit var emptyCartView: LinearLayout
    private lateinit var cartContentView: LinearLayout
    private lateinit var itemCountText: TextView

    private val cartItems = mutableListOf<CartItem>()
    private var subtotal = 0.0
    private var tax = 0.0
    private var shipping = 0.0
    private var total = 0.0

    data class CartItem(
        val id: Int,
        val name: String,
        val description: String,
        val price: Double,
        var quantity: Int,
        val emoji: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize views
        cartItemsContainer = findViewById(R.id.cartItemsContainer)
        subtotalText = findViewById(R.id.subtotalText)
        taxText = findViewById(R.id.taxText)
        shippingText = findViewById(R.id.shippingText)
        totalText = findViewById(R.id.totalText)
        checkoutButton = findViewById(R.id.checkoutButton)
        clearCartButton = findViewById(R.id.clearCartButton)
        continueShoppingButton = findViewById(R.id.continueShoppingButton)
        emptyCartView = findViewById(R.id.emptyCartView)
        cartContentView = findViewById(R.id.cartContentView)
        itemCountText = findViewById(R.id.itemCountText)

        // Initialize with sample cart items
        initializeSampleData()

        // Setup button listeners
        checkoutButton.setOnClickListener {
            showCheckoutDialog()
        }

        clearCartButton.setOnClickListener {
            showClearCartDialog()
        }

        continueShoppingButton.setOnClickListener {
            finish() // Go back to previous activity
        }

        findViewById<Button>(R.id.startShoppingButton).setOnClickListener {
            addSampleItems()
        }

        // Start animations
        animateColorScale()
        updateCartUI()
    }

    private fun initializeSampleData() {
        // Add sample items to cart
        cartItems.clear()
        cartItems.add(CartItem(1, "Android Course", "Complete Mobile Development", 49.99, 1, "📱"))
        cartItems.add(CartItem(2, "Kotlin Tutorial", "Master Kotlin Programming", 39.99, 2, "💻"))
        cartItems.add(CartItem(3, "UI/UX Design", "Beautiful App Interfaces", 29.99, 1, "🎨"))
        cartItems.add(CartItem(4, "Quiz Pro License", "Unlimited Quiz Access", 19.99, 1, "🎯"))
    }

    private fun addSampleItems() {
        cartItems.clear()
        cartItems.add(CartItem(1, "Android Course", "Complete Mobile Development", 49.99, 1, "📱"))
        cartItems.add(CartItem(2, "Kotlin Tutorial", "Master Kotlin Programming", 39.99, 2, "💻"))
        cartItems.add(CartItem(3, "UI/UX Design", "Beautiful App Interfaces", 29.99, 1, "🎨"))
        cartItems.add(CartItem(4, "Quiz Pro License", "Unlimited Quiz Access", 19.99, 1, "🎯"))
        cartItems.add(CartItem(5, "Database Mastery", "Learn SQLite & Room DB", 34.99, 1, "🗄️"))
        updateCartUI()
        Toast.makeText(this, "Items added to cart!", Toast.LENGTH_SHORT).show()
    }

    private fun updateCartUI() {
        if (cartItems.isEmpty()) {
            emptyCartView.visibility = View.VISIBLE
            cartContentView.visibility = View.GONE
        } else {
            emptyCartView.visibility = View.GONE
            cartContentView.visibility = View.VISIBLE
            renderCartItems()
            calculateTotals()
        }
    }

    private fun renderCartItems() {
        cartItemsContainer.removeAllViews()

        cartItems.forEachIndexed { index, item ->
            val itemView = LayoutInflater.from(this).inflate(R.layout.cart_item_card, cartItemsContainer, false)

            // Set item data
            itemView.findViewById<TextView>(R.id.itemEmoji).text = item.emoji
            itemView.findViewById<TextView>(R.id.itemName).text = item.name
            itemView.findViewById<TextView>(R.id.itemDescription).text = item.description
            itemView.findViewById<TextView>(R.id.itemPrice).text = "$${String.format("%.2f", item.price)}"
            itemView.findViewById<TextView>(R.id.itemQuantity).text = item.quantity.toString()
            itemView.findViewById<TextView>(R.id.itemTotal).text = "$${String.format("%.2f", item.price * item.quantity)}"

            // Setup quantity controls
            val increaseBtn = itemView.findViewById<ImageButton>(R.id.increaseQuantityBtn)
            val decreaseBtn = itemView.findViewById<ImageButton>(R.id.decreaseQuantityBtn)
            val removeBtn = itemView.findViewById<ImageButton>(R.id.removeItemBtn)

            increaseBtn.setOnClickListener {
                item.quantity++
                updateCartUI()
                animateButton(it)
            }

            decreaseBtn.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    updateCartUI()
                    animateButton(it)
                } else {
                    Toast.makeText(this, "Quantity can't be less than 1", Toast.LENGTH_SHORT).show()
                }
            }

            removeBtn.setOnClickListener {
                showRemoveItemDialog(item)
            }

            // Add item with animation
            cartItemsContainer.addView(itemView)
            animateItemEntry(itemView, index)
        }

        // Update item count
        itemCountText.text = "${cartItems.size} Items"
    }

    private fun calculateTotals() {
        subtotal = cartItems.sumOf { it.price * it.quantity }
        tax = subtotal * 0.08 // 8% tax
        shipping = if (subtotal > 100) 0.0 else 5.99
        total = subtotal + tax + shipping

        // Update UI with animation
        animateTextChange(subtotalText, "$${String.format("%.2f", subtotal)}")
        animateTextChange(taxText, "$${String.format("%.2f", tax)}")
        animateTextChange(shippingText, if (shipping == 0.0) "FREE" else "$${String.format("%.2f", shipping)}")
        animateTextChange(totalText, "$${String.format("%.2f", total)}")
    }

    private fun showCheckoutDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_checkout, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.checkoutNameInput)
        val addressInput = dialogView.findViewById<EditText>(R.id.checkoutAddressInput)
        val cardInput = dialogView.findViewById<EditText>(R.id.checkoutCardInput)

        val dialog = AlertDialog.Builder(this, R.style.RoundedDialog)
            .setTitle("🛒 Checkout")
            .setMessage("Complete your purchase")
            .setView(dialogView)
            .setPositiveButton("PLACE ORDER", null)
            .setNegativeButton("CANCEL", null)
            .create()

        dialog.show()

        // Override positive button to validate
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val address = addressInput.text.toString().trim()
            val card = cardInput.text.toString().trim()

            if (name.isEmpty()) {
                nameInput.error = "Name is required"
                Toast.makeText(this, "⚠️ Please enter your name", Toast.LENGTH_SHORT).show()
            } else if (address.isEmpty()) {
                addressInput.error = "Address is required"
                Toast.makeText(this, "⚠️ Please enter your address", Toast.LENGTH_SHORT).show()
            } else if (card.isEmpty() || card.length < 16) {
                cardInput.error = "Valid card number required"
                Toast.makeText(this, "⚠️ Please enter a valid card number", Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                showOrderConfirmation(name)
            }
        }

        // Customize button colors
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(android.R.color.darker_gray, null))
    }

    private fun showOrderConfirmation(name: String) {
        val message = """
            Thank you, $name! 🎉
            
            Order Total: $${String.format("%.2f", total)}
            Items: ${cartItems.size}
            
            Your order has been placed successfully!
            Expected delivery: 3-5 business days
        """.trimIndent()

        AlertDialog.Builder(this, R.style.RoundedDialog)
            .setTitle("✅ Order Confirmed")
            .setMessage(message)
            .setPositiveButton("VIEW ORDER") { dialog, _ ->
                dialog.dismiss()
                cartItems.clear()
                updateCartUI()
                Toast.makeText(this, "Order #${System.currentTimeMillis() % 10000} confirmed!", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("CLOSE") { dialog, _ ->
                dialog.dismiss()
                cartItems.clear()
                updateCartUI()
            }
            .setCancelable(false)
            .show()
    }

    private fun showClearCartDialog() {
        AlertDialog.Builder(this, R.style.RoundedDialog)
            .setTitle("🗑️ Clear Cart")
            .setMessage("Are you sure you want to remove all items from your cart?")
            .setPositiveButton("CLEAR ALL") { dialog, _ ->
                cartItems.clear()
                updateCartUI()
                Toast.makeText(this, "Cart cleared!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("CANCEL", null)
            .show()
    }

    private fun showRemoveItemDialog(item: CartItem) {
        AlertDialog.Builder(this, R.style.RoundedDialog)
            .setTitle("${item.emoji} Remove Item")
            .setMessage("Remove \"${item.name}\" from your cart?")
            .setPositiveButton("REMOVE") { dialog, _ ->
                cartItems.remove(item)
                updateCartUI()
                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("KEEP", null)
            .show()
    }

    // ========== ANIMATIONS ==========

    private fun animateColorScale() {
        val colorScale = findViewById<LinearLayout>(R.id.colorScale)
        for (i in 0 until colorScale.childCount) {
            val child = colorScale.getChildAt(i)
            val anim = ScaleAnimation(
                0f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 600
                startOffset = i * 100L
                interpolator = AccelerateDecelerateInterpolator()
            }
            child.startAnimation(anim)
        }
    }

    private fun animateItemEntry(view: View, index: Int) {
        val translateAnim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        ).apply {
            duration = 400
            startOffset = index * 80L
        }

        val alphaAnim = AlphaAnimation(0f, 1f).apply {
            duration = 400
            startOffset = index * 80L
        }

        val animSet = AnimationSet(true).apply {
            addAnimation(translateAnim)
            addAnimation(alphaAnim)
            interpolator = AccelerateDecelerateInterpolator()
        }

        view.startAnimation(animSet)
    }

    private fun animateButton(view: View) {
        val scaleDown = ScaleAnimation(
            1f, 0.9f, 1f, 0.9f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 100
            fillAfter = true
        }

        val scaleUp = ScaleAnimation(
            0.9f, 1f, 0.9f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 100
            startOffset = 100
        }

        val animSet = AnimationSet(false).apply {
            addAnimation(scaleDown)
            addAnimation(scaleUp)
        }

        view.startAnimation(animSet)
    }

    private fun animateTextChange(textView: TextView, newText: String) {
        val fadeOut = AlphaAnimation(1f, 0f).apply {
            duration = 150
        }

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                textView.text = newText
                val fadeIn = AlphaAnimation(0f, 1f).apply {
                    duration = 150
                }
                textView.startAnimation(fadeIn)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        textView.startAnimation(fadeOut)
    }
}

