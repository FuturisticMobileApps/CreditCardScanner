hi 
# Fast, Lightweight, and Offline Credit Card Scanner Android Library

This Android library offers a fast, lightweight, and offline solution for credit/debit card scanning. Leveraging Firebase ML Kit and CameraX technologies, it effortlessly extracts crucial card details including the card number, type, and expiry date. Seamlessly integrate this solution into your Android projects to enhance user experiences and simplify payment processes.

## Build with

Kotlin,
XML,
Material Components,
Coroutine,
CameraX
and
Firebase ML Kit,
## Features

- **Efficient Scanning**  Utilizes Firebase ML Kit and CameraX for efficient and accurate credit card scanning.

- **Offline Functionality** - Works offline, ensuring privacy and reliability without requiring constant internet connectivity.

- **Fast Extraction** - Quickly extracts essential card details such as the card number, type, and expiry date for seamless payment processing.

- **Lightweight Integration** - Lightweight and easy-to-integrate library, minimizing overhead and simplifying project setup.

- **User-Friendly Experience** - Enhances user experiences by providing a smooth and intuitive credit card scanning interface.

- **Integrate in Java and Kotlin Projects** Compatible with both Java and Kotlin, allowing integration into a wide range of Android projects.

- **Robust Compatibility** - Compatible with a wide range of Android devices, ensuring broad accessibility and usability.

## How to

**Step 1** - Add the JitPack repository to your build file

```groovy
 repositories {
	mavenCentral()
	maven { url 'https://jitpack.io' }
 }

```

**Step 2** - Add the dependency
```groovy
 dependencies  {
	implementation 'com.github.FuturisticMobileApps:CreditCardScanner:1.0'
 }
```

**Step 3** - Implement Scanner

**In Kotlin**
```Kotlin
  CardScanner(object : CreditCardDetails {
     override fun cardDetails(cardDetails: CardDetails) {
        val expiryDate = cardDetails.expiryDate.split("/") 
        val cardNumber = cardDetails.cardNumber
        val expiryMonth = expiryDate[0]
        val expiryYear = expiryDate[1]
        val cardIconDrawable = cardDetails.cardIcon
     }
    }).show(supportFragmentManager, "CreditCardScannerDialog")
```

**In Java**
```Java
  new CardScanner(new CreditCardDetails() {
            @Override
            public void cardDetails(@NonNull CardDetails cardDetails) {
                String[] expiryDate = cardDetails.getExpiryDate().split("/");
                String cardNumber = cardDetails.getCardNumber();
                String expiryMonth = expiryDate[0];
                String expiryYear = expiryDate[1];
                int cardIconDrawable = cardDetails.getCardIcon();
            }
        }).show(supportFragmentManager, "CreditCardScannerDialog");
```




## Demo

https://github.com/FuturisticMobileApps/CreditCardScanner/assets/111674274/021cae13-e72c-4138-ab1b-6187405c7e38

### If the camera is not accessible

https://github.com/FuturisticMobileApps/CreditCardScanner/assets/111674274/06b8593e-3a68-40be-8bd2-68bd07a36ada

## Credit/Debit Card Scanner

![Credit/Debit card scanner](https://github.com/FuturisticMobileApps/CreditCardScanner/assets/111674274/019b8065-aade-49a3-9688-0bd7420b1e92)


