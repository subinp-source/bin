(function () {
    var networkChart = window.zk.Widget.$(jq('$integrationbackofficeNetworkChart')[0])._networkchart;
    networkChart.setOptions({layout: {hierarchical: false}});
    var originalScale = networkChart.getScale();
    var eventHandlers = function () {
        $("button.y-btn-primary.yw-integrationbackoffice-modal-button.z-button").on("click", function (e) {
            var canvas = $("canvas");
            if (!canvas.is(":visible")) {
                e.preventDefault();
                return;
            }
            var container = $(".yw-integrationbackoffice-metadata-visualizer.z-networkchart")[0];
            var initialHeight = container.offsetHeight;
            var initialWidth = container.offsetWidth;
            var scale = networkChart.getScale();
            var viewPoint = networkChart.getViewPosition();
            networkChart.fit();
            var changedWidth = initialWidth / originalScale;
            var changedHeight = initialHeight / originalScale;
            var newCSS = "width:" + changedWidth + "px !important; height:" + changedHeight + "px !important;";
            $(container).css("cssText", newCSS);
            networkChart.redraw();
            networkChart.fit();
            var canvasServer = canvas[0];
            var imageDataURL = canvasServer.toDataURL('image/png');
            var canvasAsString = window.zk.Widget.$(jq('$canvasAsString')[0]);
            canvasAsString.setValue(imageDataURL);
            canvasAsString.fireOnChange();
            $(container).css("cssText", "width:" + initialWidth + "px !important;" + "height:" + initialHeight + "px !important;");
            networkChart.redraw();
            networkChart.fit();
            networkChart.moveTo({position: viewPoint, scale: scale});
        });
    };

    $('div.vis-network div.vis-navigation div.vis-button.vis-up:first').prop('title', 'Up');
    $('div.vis-network div.vis-navigation div.vis-button.vis-down:first').prop('title', 'Down');
    $('div.vis-network div.vis-navigation div.vis-button.vis-left:first').prop('title', 'Left');
    $('div.vis-network div.vis-navigation div.vis-button.vis-right:first').prop('title', 'Right');
    $('div.vis-network div.vis-navigation div.vis-button.vis-zoomIn:first').prop('title', 'Zoom In');
    $('div.vis-network div.vis-navigation div.vis-button.vis-zoomOut:first').prop('title', 'Zoom Out');
    $('div.vis-network div.vis-navigation div.vis-button.vis-zoomExtends:first').prop('title', 'Fit');

    eventHandlers();
})();