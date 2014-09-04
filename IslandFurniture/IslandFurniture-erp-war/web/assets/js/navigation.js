(function(a) {
    (jQuery.browser = jQuery.browser || {}).mobile })(navigator.userAgent || navigator.vendor || window.opera);
var myApplication = function() {
    var linkLocation, searchOpen = false,
        app = $(".app"),
        maxHeight = 0;

    function redirectPage() {
        window.location = linkLocation
    }

    function events() {
        $(window).resize(function() {
            if (!$.browser.mobile && !checkBreakout()) {
                $(".main-navigation").slimScroll({
                    height: "auto"
                });
                $(".slimscroll").slimScroll({
                    height: "auto"
                });
            }
        });
    }

    function checkBreakout() {
        var state = false;
        if (app.hasClass("small-menu")) {
            state = true
        }
        return state
    }

    function initFFFix() {
        if (navigator.userAgent.search("Firefox") >= 0) {
            $(".layout > aside, .layout > section").wrapInner('<div class="fffix"/>')
        }
    }

    function initMenuCollapse() {
        $(document).on("click", ".main-navigation li > a", function(e) {
            var subMenu = $(this).next(),
                parent = $(this).closest("li");
            if (!subMenu.hasClass("sub-menu")) {
                return
            }
            if (checkBreakout() && $(window).width() > 767) {
                return
            }
            $(".sidebar li").removeClass("open");
            parent.addClass("open");
            if ((subMenu.is("ul")) && (subMenu.is(":visible")) && (!app.hasClass("small-sidebar"))) {
                parent.removeClass("open");
                subMenu.slideUp()
            }
            if ((subMenu.is("ul")) && (!subMenu.is(":visible")) && (!app.hasClass("small-sidebar"))) {
                $(".sidebar ul ul:visible").slideUp();
                subMenu.slideDown();
                parent.addClass("open")
            }
            if (subMenu.is("ul")) {
                return false
            }
            e.stopPropagation();
            return true
        });
        $(".main-navigation > .nav > li.open").each(function() {
            $(".sub-menu").hide();
            $(this).children(".sub-menu").show()
        });
        $(".main-navigation, .slimscroll").each(function() {
            if (checkBreakout() || app.hasClass("fixed-scroll") || $.browser.mobile) {
                return
            }
            var data = $(this).data();
            $(this).slimScroll(data)
        })
    }

    function initToggleSidebar() {
        $(document).on("click", ".toggle-sidebar", function(e) {
            if (app.hasClass("small-menu")) {
                app.removeClass("small-menu");
                if (!$.browser.mobile && $.fn.slimScroll) {
                    $(".main-navigation").each(function() {
                        var data = $(this).data();
                        $(this).slimScroll(data)
                    })
                }
            } else {
                app.addClass("small-menu");
                if (!$.browser.mobile && $.fn.slimScroll) {
                    $(".main-navigation").slimScroll({
                        destroy: true
                    }).removeAttr("style")
                }
            }
            e.preventDefault()
        })
    }

    function initIcheck() {
        if ($.isFunction($.fn.iCheck)) {
            $(".icheck").iCheck()
        }
    }
    return {
        init: function() {
            events();
            initFFFix();
            initMenuCollapse();
            initToggleSidebar();
            initIcheck()
        }
    }
}();
$(function() {
    myApplication.init()
});