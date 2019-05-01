package com.books.chapters.restfulapi.patterns.parttwo.bpm;

public final class ProcessConstants {

	public static final String PROCESS_KEY_SUBMIT_SC = "submitShoppingCart";

	public static final String VAR_CTX = "context";
	public static final String VAR_SC_ID = "shoppingCartId";
	public static final String VAR_ADDRESS = "address";
	public static final String VAR_PAYMENT = "payment";
	public static final String VAR_SC = "shoppingCart";
	public static final String VAR_PRODUCT = "product";
	public static final String VAR_RESPONSE = "response";
	public static final String VAR_PAYMENT_RESERVED = "paymentReserved";
	public static final String VAR_INVENTORY_ALLOCATED = "inventoryAllocated";
	public static final String VAR_ORDER_PLACED = "orderPlaced";

	public static final String SERVICE_NAME_LOCATION = "LocationService";
	public static final String SERVICE_NAME_PAYMENT = "PaymentService";
	public static final String SERVICE_NAME_INVENTORY = "InventoryService";
	public static final String SERVICE_NAME_ORDER = "OrderService";
	public static final String SERVICE_NAME_CUSTOMER = "CustomerService";
	public static final String SERVICE_NAME_BACKOFFICE = "BackofficeService";
	public static final String SERVICE_NAME_SHOPPINGCART = "ShoppingCartService";

	public static final String ENTITY_TYPE_SHOPPINGCART = "SHOPPINGCART";
	public static final String ENTITY_TYPE_LOCATION = "LOCATION";
	public static final String ENTITY_TYPE_PAYMENT = "PAYMENT";
	public static final String ENTITY_TYPE_PRODUCT = "PRODUCT";
	public static final String ENTITY_TYPE_ERROR = "ERROR";

	public static final String SC_STATUS_OPEN = "OPEN";
	public static final String SC_STATUS_CLOSED = "CLOSED";

	public static final String UNKNOWN = "UNKNOWN";

	private ProcessConstants() {
	}
}
