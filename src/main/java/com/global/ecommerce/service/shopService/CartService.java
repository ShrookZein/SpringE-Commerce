package com.global.ecommerce.service.shopService;

import com.global.ecommerce.Entity.shopEntity.Cart;
import com.global.ecommerce.Entity.shopEntity.CartItem;
import com.global.ecommerce.Entity.shopEntity.Product;
import com.global.ecommerce.DTO.shopDto.ProductResponseDto;
import com.global.ecommerce.repository.shopRepo.CartItemRepo;
import com.global.ecommerce.repository.shopRepo.CartRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class CartService {
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ProductService productService;

    public Cart createCart(Long userId) {
        log.info("Creating cart for userId: {}", userId);
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCartItems(new ArrayList<>());
        Cart savedCart = cartRepo.save(cart);
        log.info("Cart created with id: {} for userId: {}", savedCart.getId(), userId);
        return savedCart;
    }

    public Optional<Cart> getCartByUserId(Long userId) {
        log.info("Fetching cart for userId: {}", userId);
        return cartRepo.findByUserId(userId);
    }

    @Transactional
    public Cart addItemToCart(Long userId, Long productId, Long quantity) {
        log.info("Adding item to cart. userId: {}, productId: {}, quantity: {}", userId, productId, quantity);
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        Cart cart = getCartByUserId(userId).orElseGet(() -> createCart(userId));
//      if product item already exist in cart will update his quantity
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                Cart updatedCart = cartRepo.save(cart);
                log.info("Updated item quantity in cart for productId: {}", productId);
                return updatedCart;
            }
        }

        Product product = Product.builder()
                .id(productResponseDto.getId())
                .name(productResponseDto.getName())
                .description(productResponseDto.getDescription())
                .price(productResponseDto.getPrice()).build();
        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(quantity)
                .cart(cart)
                .build();
        cartItemRepo.save(cartItem);
        cart.getCartItems().add(cartItem);
        Cart updatedCart = cartRepo.save(cart);
        log.info("Added new item to cart for productId: {}", productId);
        return updatedCart;
    }

    public List<CartItem> getItemsByCartId(Long cartId) {
        log.info("Fetching items for cartId: {}", cartId);
        List<CartItem> cartItems = cartItemRepo.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            log.error("Cart not found with id: {}", cartId);
            throw new RuntimeException("Cart not found with id: " + cartId);
        }
        return cartItems;
    }

    public List<CartItem> getItemsByUserId(Long userId) {
        log.info("Fetching items for userId: {}", userId);
        return cartRepo.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found for userId: {}", userId);
                    return new RuntimeException("Cart not found for userId: " + userId);
                })
                .getCartItems();
    }

    public Cart deleteAllItemsInCart(Long userId) {
        log.info("Deleting all items in cart for userId: {}", userId);
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found for userId: {}", userId);
                    return new RuntimeException("Cart not found for userId: " + userId);
                });
        cart.getCartItems().clear();
        Cart updatedCart = cartRepo.save(cart);
        log.info("All items deleted from cart for userId: {}", userId);
        return updatedCart;
    }

    @Transactional
    public Cart deleteItemFromCart(Long userId, Long productId) {
        log.info("Deleting item from cart. userId: {}, productId: {}", userId, productId);
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found for userId: {}", userId);
                    return new RuntimeException("Cart not found for userId: " + userId);
                });
        CartItem itemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Item not found in cart for productId: {}", productId);
                    return new RuntimeException("Item not found in the cart with id: " + productId);
                });
        cart.getCartItems().remove(itemToRemove);
        cartRepo.save(cart);
        cartItemRepo.deleteById(itemToRemove.getId());
        log.info("Item deleted from cart for productId: {}", productId);
        return cart;
    }

    @Transactional
    public Cart updateCartItem(Long userId, Long productId, Long quantity) {
        log.info("Updating cart item. userId: {}, productId: {}, quantity: {}", userId, productId, quantity);
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found for userId: {}", userId);
                    return new RuntimeException("Cart not found for userId: " + userId);
                });
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Item not found in cart for productId: {}", productId);
                    return new RuntimeException("Item not found in the cart with product id: " + productId);
                });
        cartItem.setQuantity(quantity);
        cartItemRepo.save(cartItem);
        log.info("Cart item updated for productId: {}, new quantity: {}", productId, quantity);
        return cartRepo.save(cart);
    }
}









//
//package com.global.ecommerce.service.shopService;
//
//import com.global.ecommerce.Entity.shopEntity.Cart;
//import com.global.ecommerce.Entity.shopEntity.CartItem;
//import com.global.ecommerce.Entity.shopEntity.Product;
//import com.global.ecommerce.DTO.shopDto.ProductResponseDto;
//import com.global.ecommerce.repository.shopRepo.CartItemRepo;
//import com.global.ecommerce.repository.shopRepo.CartRepo;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//@NoArgsConstructor
//public class CartService {
//    @Autowired
//    private CartRepo cartRepo;
//    @Autowired
//    private CartItemRepo cartItemRepo;
//    @Autowired
//    private ProductService productService;
//    public Cart createCart(Long userId) {
//        Cart cart=new Cart();
//        cart.setUserId(userId);
//        cart.setCartItems(new ArrayList<>());
//        return cartRepo.save(cart);
//    }
//    public Optional<Cart> getCartByUserId(Long userId) {
//        return cartRepo.findByUserId(userId);
//    }
//    //Todo: revision @NotNull on Cart in CartItem Entity Will give Error With this function or No
//    @Transactional
//    public Cart addItemToCart(Long userId,Long productId,Long quantity){
////        Optional<Product>product=productService.getProductById(productId);
//        ProductResponseDto productResponseDto =productService.getProductById(productId);
//        Cart cart=getCartByUserId(userId).orElseGet(()->createCart(userId));
//        //Make sure the item has not been added before.
//        for(CartItem item:cart.getCartItems()){
//            if(item.getProduct().getId().equals(productId)){
//                item.setQuantity(item.getQuantity()+quantity);
//                return cartRepo.save(cart);
//            }
//        }
////        cartRepo.save(cart);
//        Product product=Product.builder()
//                .id(productResponseDto.getId())
//                .name(productResponseDto.getName())
//                .description(productResponseDto.getDescription())
//                .price(productResponseDto.getPrice()).build();
//        CartItem cartItem=CartItem.builder()
//                .product(product)
//                .quantity(quantity)
//                .cart(cart)
//                .build();
//        cartItemRepo.save(cartItem);
////        Cart cart=getCartByUserId(userId).orElseGet(()->createCart(userId));
//        cart.getCartItems().add(cartItem);
//        return cartRepo.save(cart);
//    }
//
//    //Todo: كتبتها بطريقة تاني ارجع اتأكد انها صح
//    public List<CartItem>getItemsByCartId(Long cartId){
////        return cartRepo.findById(cartId)
////                .orElseThrow(() -> new RuntimeException("Cart not found with id: "+cartId)).getCartItems();
//        List<CartItem>cartItems=cartItemRepo.findByCartId(cartId);
//        if(cartItems.isEmpty()){
//            throw new RuntimeException("Cart not found with id: "+cartId);
//        }
//        return cartItems;
//    }
//    public List<CartItem> getItemsByUserId(Long userId) {
//        return cartRepo.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Cart not found for user id: "+userId))
//                .getCartItems();
//    }
//    public Cart deleteAllItemsInCart(Long userId) {
//        Cart cart = cartRepo.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Cart not found for user id: "+userId));
//        cart.getCartItems().clear();
//        return cartRepo.save(cart);
//    }
//    @Transactional
//    public Cart deleteItemFromCart(Long userId, Long productId) {
//        Cart cart = cartRepo.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Cart not found for user id: "+userId));
//        CartItem itemToRemove = null;
//        System.out.println(cart.getCartItems().get(2).getId());
//        for (CartItem item : cart.getCartItems()) {
//            if (item.getProduct().getId().equals(productId)) {
//                System.out.println(item.getId());
//                itemToRemove = item;
//                break;
//            }
//        }
//        System.out.println(itemToRemove.getId());
//        if (itemToRemove != null) {
//            cart.getCartItems().remove(itemToRemove);
//        } else {
//            throw new RuntimeException("Item not found in the cart with id: " + productId);
//        }
//        cartRepo.save(cart); // Save the cart first to update the relationship
//        cartItemRepo.deleteById(itemToRemove.getId()); // Then delete the cart item
//        return cart;
//    }
//
//    // Not need Product Id i Will delete It
//    //revision this function
//    @Transactional
//    public Cart updateCartItem(Long userId, Long productId, Long quantity) {
//        Cart cart = cartRepo.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Cart not found for user id: "+userId));
//        List<CartItem> cartItems = cart.getCartItems();
//
//        Optional<CartItem> cartItem = cartItems.stream()
//                .filter(item -> item.getProduct().getId().equals(productId))
//                .findFirst();
//
//        if (cartItem.isPresent()) {
//            CartItem cartItem1 = cartItem.get();
////            cartItem1.setProduct(updatedCartItem.getProduct());
//            cartItem1.setQuantity(quantity);
//            cartItemRepo.save(cartItem1);
//        } else {
//            throw new RuntimeException("Item not found in the cart with product id: "+productId);
//        }
//
//        return cartRepo.save(cart);
//    }
//
//}
//
//
//
//
