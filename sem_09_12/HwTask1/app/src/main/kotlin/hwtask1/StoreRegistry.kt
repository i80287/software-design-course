package hwtask1;

import java.util.ArrayList;
import java.util.HashMap;
import kotlin.text.StringBuilder;
import kotlinx.serialization.Serializable;

const val STORE_REGISTRY_FILESTORAGE = "store_reg.json";

public class StoreRegistry {
    @Serializable
    public data class StoreItem(public val itemName: String, public var quantity: Int);

    @Serializable
    private val items: HashMap<String, StoreItem> = RegistryMapSerializer(STORE_REGISTRY_FILESTORAGE).deserialize<StoreItem>();

    private val goodsCart = ArrayList<StoreItem>();

    public fun saveRegistryState() {
        RegistryMapSerializer(STORE_REGISTRY_FILESTORAGE).serialize<StoreItem>(items);
    }

    public fun addItemIfNotExists(itemName: String, quantity: Int): Boolean {
        val ret: StoreItem? = items.putIfAbsent(itemName, StoreItem(itemName, quantity));
        val insertedNewItem: Boolean = ret == null;
        return insertedNewItem;
    }

    public fun buyItem(itemName: String): Boolean = buyItem(itemName, 1);

    public fun buyItem(itemName: String, quantity: Int): Boolean {
        val item: StoreItem? = items.get(itemName);
        if (item == null || item.quantity < quantity) {
            return false;
        }

        item.quantity -= quantity
        if (item.quantity == 0) {
            items.remove(itemName);
        }

        goodsCart.add(StoreItem(itemName, quantity));
        return true;
    }

    public fun itemsToString(): String {
        if (!items.isNotEmpty()) {
            return "+-------------+" +
                   "| Empty Store |" +
                   "+-------------+";
        }

        var maxNameLength: Int = 0;
        var maxQuantity: Int = 0;
        for (item in items.values) {
            val itemNameLen: Int = item.itemName.length;
            if (itemNameLen > maxNameLength) {
                maxNameLength = itemNameLen; 
            }

            val quantity: Int = item.quantity;
            if (quantity > maxQuantity) {
                maxQuantity = quantity;
            }
        }

        val maxQuantityStrLen: Int = maxQuantity.toString().length;
        val eachRowLength: Int = 2 + maxNameLength + 3 + maxQuantityStrLen + 3;
        val totalRows: Int = (items.values.size shl 1) or 1;
        val totalLength: Int = totalRows * eachRowLength;
        val separatingLine: String = '+' + "-".repeat(maxNameLength + 2) + '+' + "-".repeat(maxQuantityStrLen + 2) + "+\n";

        val sb = StringBuilder(totalLength);
        sb.append(separatingLine);
        for (item in items.values) {
            sb.append("| ");
            sb.append(item.itemName);
            sb.append(" ".repeat(maxNameLength - item.itemName.length));
            sb.append(" | ");
            val quantityStr: String = item.quantity.toString();
            sb.append(quantityStr);
            sb.append(" ".repeat(maxQuantityStrLen - quantityStr.length))
            sb.append(" |\n");
            sb.append(separatingLine);
        }

        // Drop last '\n' char
        sb.dropLast(1);
        return sb.toString();
    }

    public fun makePurchase(payingCardNum: Int): Boolean {
        val cardWasEmpty: Boolean = goodsCart.isEmpty();
        goodsCart.clear();
        return !cardWasEmpty; 
    }
};
