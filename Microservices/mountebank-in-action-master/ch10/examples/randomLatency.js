function () {
  var slowdown = Math.random() > 0.9,
    multiplier = slowdown ? 10000 : 1000;
  return Math.floor(Math.random() * multiplier);
}
