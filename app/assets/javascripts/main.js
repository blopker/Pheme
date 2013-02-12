requirejs.config({
  paths: {
    'datatypes': 'datatype/datatypes'
  }
});
require(['lib/router', 'lib/domReady!'], function(router) {
  'use strict';
  if (router[window.location.pathname]) {
    router[window.location.pathname]();
  }
});

