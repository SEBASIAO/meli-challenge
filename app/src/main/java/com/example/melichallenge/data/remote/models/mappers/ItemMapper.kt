package com.example.melichallenge.data.remote.models.mappers

import com.example.melichallenge.data.remote.models.AddressApiModel
import com.example.melichallenge.data.remote.models.ItemApiModel
import com.example.melichallenge.data.remote.models.PictureApiModel
import com.example.melichallenge.data.remote.models.SalePriceApiModel
import com.example.melichallenge.data.remote.models.SellerApiModel
import com.example.melichallenge.data.remote.models.ShippingApiModel
import com.example.melichallenge.domain.models.Address
import com.example.melichallenge.domain.models.Item
import com.example.melichallenge.domain.models.PictureModel
import com.example.melichallenge.domain.models.SalePrice
import com.example.melichallenge.domain.models.Seller
import com.example.melichallenge.domain.models.Shipping

fun ItemApiModel.toBo() = Item(
    id = this.id,
    title = this.title,
    price = this.price,
    originalPrice = this.originalPrice,
    condition = this.condition,
    seller = this.seller.toBo(),
    address = this.address.toBo(),
    shipping = this.shipping.toBo(),
    salePrice = this.salePrice.toBo(),
    thumbnail = this.thumbnail,
    pictures = this.pictures?.map { it.toBo() },
    secureThumbnail = this.secureThumbnail,
    basePrice = this.basePrice,
    soldQuantity = this.soldQuantity
)

fun SellerApiModel?.toBo() = Seller(
    id = this?.id,
    nickname = this?.nickname,
)

fun AddressApiModel?.toBo() = Address(
    stateId = this?.stateId,
    stateName = this?.stateName,
    cityId = this?.cityId,
    cityName = this?.cityName,
)

fun ShippingApiModel?.toBo() = Shipping(
    storePickUp = this?.storePickUp,
    freeShipping = this?.freeShipping
)

fun SalePriceApiModel?.toBo() = SalePrice(
    priceId = this?.priceId,
    amount = this?.amount,
    currencyId = this?.currencyId,
    exchangeRate = this?.exchangeRate,
)

fun PictureApiModel?.toBo() = PictureModel(
    id = this?.id,
    url = this?.url,
)