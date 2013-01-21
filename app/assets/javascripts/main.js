require(["lib/router"], function(router) {
  $(function() {
    if (router[window.location.pathname]) {
      router[window.location.pathname]();
    }
  });
});

