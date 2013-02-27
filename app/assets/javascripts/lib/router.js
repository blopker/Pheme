
/*
  Returns function of first matched route. Put most specific route higher on list.
*/

define(['logs', 'component'], function(logs, component) {
  'use strict';

  var routes = {
    '/logs': logs.run,
    '/computers/': component.run
  };

  function selectRoute (url) {
    var func = function() {};
    $.each(routes, function(key, val) {
      if (url.indexOf(key) !== -1) {
        func = val;
      }
    });
    return func;
  }

  return selectRoute;
});
