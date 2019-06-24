/**
 * @param {org.fsn.testnetwork.Order} order - order food product
 * @transaction
 */
function orderFoodProduct(order) {
  order.food.consumer= order.newConsumer;
  return getAssetRegistry('org.fsn.testnetwork.Food')
    .then(function (assetRegistry) {
    return assetRegistry.update(order.food);
  });
}