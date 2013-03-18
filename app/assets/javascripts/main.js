require(['lib/router', 'lib/domReady!'], function(router) {
  'use strict';

  router(window.location.pathname)();

  // === Mobile Sidebar navigation === //

  var ul = $('#sidebar > ul');

  $('#sidebar > a').click(function(e)
  {
    e.preventDefault();
    var sidebar = $('#sidebar');
    if(sidebar.hasClass('open'))
    {
      sidebar.removeClass('open');
      ul.slideUp(250);
    } else
    {
      sidebar.addClass('open');
      ul.slideDown(250);
    }
  });

  // === Resize window related === //
  $(window).resize(function()
  {
    if($(window).width() > 479)
    {
      ul.css({'display':'block'});
      $('#content-header .btn-group').css({width:'auto'});
    }
    if($(window).width() < 479)
    {
      ul.css({'display':'none'});
    }
    if($(window).width() > 768)
    {
      $('#user-nav > ul').css({width:'auto',margin:'0'});
    }
  });

  if($(window).width() < 468)
  {
    ul.css({'display':'none'});
  }
  if($(window).width() > 479)
  {
    ul.css({'display':'block'});
  }

  // === Tooltips === //
  $('.tip').tooltip();
  $('.tip-left').tooltip({ placement: 'left' });
  $('.tip-right').tooltip({ placement: 'right' });
  $('.tip-top').tooltip({ placement: 'top' });
  $('.tip-bottom').tooltip({ placement: 'bottom' });

});

