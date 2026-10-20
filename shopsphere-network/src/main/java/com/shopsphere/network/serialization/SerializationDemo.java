package com.shopsphere.network.serialization;

import com.shopsphere.model.Product;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Educational demonstration of Java Object Serialization.
 * Demonstrates Serializable, serialVersionUID, ObjectOutputStream, ObjectInputStream,
 * and field integrity verification.
 */
public class SerializationDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println(" ShopSphere - Java Object Serialization Demonstration");
        System.out.println(" RTU Syllabus Topic: java.io.Serializable & Stream I/O");
        System.out.println("==========================================================");

        // 1. Create a sample domain object
        Product originalProduct = new Product();
        originalProduct.setProductId(501);
        originalProduct.setCategoryId(1);
        originalProduct.setName("MacBook Pro M3 Max - High Precision Unit");
        originalProduct.setBrand("Apple");
        originalProduct.setDescription("Enterprise workstation serialized for distributed transmission.");
        originalProduct.setPrice(new BigDecimal("249900.00"));
        originalProduct.setDiscount(new BigDecimal("10.00"));
        originalProduct.setStock(15);
        originalProduct.setStatus(Product.Status.ACTIVE);

        System.out.println("\n[1] Original In-Memory Product Object:");
        System.out.println("    ID: " + originalProduct.getProductId());
        System.out.println("    Name: " + originalProduct.getName());
        System.out.println("    Price: ₹" + originalProduct.getPrice());
        System.out.println("    Discounted Price: ₹" + originalProduct.getDiscountedPrice());
        System.out.println("    Stock: " + originalProduct.getStock());

        Path tempFile = null;
        try {
            // 2. Serialize object to a binary file using ObjectOutputStream
            tempFile = Files.createTempFile("shopsphere_serialized_product_", ".ser");
            System.out.println("\n[2] Serializing Product to binary stream: " + tempFile.toAbsolutePath());

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile.toFile()))) {
                oos.writeObject(originalProduct);
                oos.flush();
            }

            long byteSize = Files.size(tempFile);
            System.out.println("    SUCCESS: Object serialized successfully! (" + byteSize + " bytes written)");

            // 3. Deserialize object back using ObjectInputStream
            System.out.println("\n[3] Deserializing Product from binary stream via ObjectInputStream...");
            Product deserializedProduct;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tempFile.toFile()))) {
                Object obj = ois.readObject();
                if (obj instanceof Product) {
                    deserializedProduct = (Product) obj;
                } else {
                    throw new ClassCastException("Unexpected object type: " + obj.getClass());
                }
            }

            // 4. Verify field integrity and behavior
            System.out.println("\n[4] Deserialized Product Object Verification:");
            System.out.println("    ID: " + deserializedProduct.getProductId());
            System.out.println("    Name: " + deserializedProduct.getName());
            System.out.println("    Price: ₹" + deserializedProduct.getPrice());
            System.out.println("    Computed Discounted Price: ₹" + deserializedProduct.getDiscountedPrice());
            System.out.println("    Stock: " + deserializedProduct.getStock());

            boolean match = originalProduct.getProductId() == deserializedProduct.getProductId() &&
                    originalProduct.getName().equals(deserializedProduct.getName()) &&
                    originalProduct.getPrice().compareTo(deserializedProduct.getPrice()) == 0 &&
                    originalProduct.getStock() == deserializedProduct.getStock();

            if (match) {
                System.out.println("\n>>> OBJECT INTEGRITY VERIFICATION: PASSED (100% Match) <<<");
            } else {
                System.err.println("\n>>> OBJECT INTEGRITY VERIFICATION: FAILED <<<");
            }

            System.out.println("\n----------------------------------------------------------");
            System.out.println("Key Examiner Viva Takeaway:");
            System.out.println("1. Serialization converts an object graph into a portable byte stream.");
            System.out.println("2. serialVersionUID ensures version compatibility between JVM sender and receiver.");
            System.out.println("3. Transient fields (if any) are omitted from serialization.");
            System.out.println("4. Unlike text formats (JSON/XML), binary serialization preserves exact Java class identity and references.");
            System.out.println("----------------------------------------------------------\n");

        } catch (Exception e) {
            System.err.println("Serialization demonstration failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException ignored) {}
            }
        }
    }
}
