$(document).ready(function() {

    //User avatars
    const avatarProfile = document.getElementById("avatar-profile");
    const avatarHeader = document.getElementById("avatar-header");
    const avatarShop = document.getElementById("avatar-shop");
    const avatarProductDetail = document.getElementById("avatar-product-detail");

    if (avatarProfile){
        const userId = avatarProfile.getAttribute("data-user-id");
        const fileName = avatarProfile.getAttribute("data-file-name");
        getUserAvatar(userId, fileName, avatarProfile).then(r => {});
    }

    if (avatarHeader){
        const userId = avatarHeader.getAttribute("data-user-id");
        const fileName = avatarHeader.getAttribute("data-file-name");
        getUserAvatar(userId, fileName, avatarHeader).then(r => {});
    }

    if (avatarShop){
        const userId = avatarShop.getAttribute("data-user-id");
        const fileName = avatarShop.getAttribute("data-file-name");
        getUserAvatar(userId, fileName, avatarShop).then(r => {});
    }

    if (avatarProductDetail){
        const userId = avatarProductDetail.getAttribute("data-user-id");
        const fileName = avatarProductDetail.getAttribute("data-file-name");
        getUserAvatar(userId, fileName, avatarProductDetail).then(r => {});
    }


    const productInShop = document.querySelectorAll('.card-img-top');
    const productDetailImg = document.getElementById('product-detail-img');

    if (productInShop){
        productInShop.forEach(async (productImg) =>{
            const productId = productImg.getAttribute("data-product-id");
            const fileName = productImg.getAttribute("data-file-name");
            try {
                await getProductImage(productId, fileName, productImg);
            } catch (error) {
                console.error(`There was a problem with the get product image operation for product ID ${productId}:`, error);
            }
        });
    }

    if (productDetailImg){
        const productId = productDetailImg.getAttribute("data-product-id");
        const fileName = productDetailImg.getAttribute("data-file-name");
        getProductImage(productId, fileName, productDetailImg).then(r => {});
    }


});

async function getUserAvatar(userId, fileName, typeImage){
    try{
        const response = await fetch("/api/download/userAvatar/"+userId+"/"+fileName);
        if (!response.ok){
            console.log("User avatar was not found!!!");
            return;
        }
        const blob = await response.blob();
        const avatarUrl = URL.createObjectURL(blob);
        if (typeImage){
            typeImage.src = avatarUrl;
        }

    }catch (error){
        console.error('There was a problem with the get user Avatar operation:', error);
    }
}


async function getProductImage(productId, fileName, imageType){
    try{
        const response = await fetch("/api/download/productImg/"+productId+"/"+fileName);
        if (!response.ok){
            console.log("Product Image was not found!!!");
            return;
        }
        const blob = await response.blob();
        const productImgUrl = URL.createObjectURL(blob);

        if (imageType){
            imageType.src = productImgUrl;
        }

    }catch (error){
        console.error('There was a problem with the get product image operation:', error);
    }
}