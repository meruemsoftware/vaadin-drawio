var graph = null;
var xml = null;

function mxClientOnLoad(stylesheet) {

}

function load(data) {
    xml = data;
    xml = decodeURIComponent(xml);

    // Removes all illegal control characters before parsing
    var checked = [];

    for (var i = 0; i < xml.length; i++) {
        var code = xml.charCodeAt(i);

        // Removes all control chars except TAB, LF and CR
        if (code >= 32 || code == 9 || code == 10 || code == 13) {
            checked.push(xml.charAt(i));
        }
    }

    xml = checked.join('');

    var div = document.createElement('div');
    div.style.width = '100%';
    div.style.height = '100%';
    div.style.position = 'relative';
    document.body.appendChild(div);
    graph = new mxGraph(div);

    graph.resetViewOnRootChange = false;
    graph.foldingEnabled = false;
    // NOTE: Tooltips require CSS
    graph.setTooltips(false);
    graph.setEnabled(false);

    var xmlDoc = mxUtils.parseXml(xml);
    var codec = new mxCodec(xmlDoc);
    codec.decode(codec.document.documentElement, graph.getModel());
    graph.maxFitScale = 1;
    graph.fit();
    graph.center(true, false);

    window.addEventListener('resize', function () {
        graph.fit();
        graph.center(true, false);
    });
}

function edit(data) {
    load(data);

    var receive = function (evt) {
        if (evt.data == 'ready') {
            var iframe = document.getElementById("drawio-iframe").firstElementChild;
            iframe.contentWindow.postMessage(xml, '*');

        }
        else if (evt.data.length > 0) {
            // Update the graph
            var xmlDoc = mxUtils.parseXml(evt.data);
            var codec = new mxCodec(xmlDoc);
            codec.decode(codec.document.documentElement, graph.getModel());
            graph.fit();
            graph.center(true, false);

            var data = encodeURIComponent(evt.data);
            window.saved(data);
        }
        //if exit
        else {
            window.exited();
            window.removeEventListener('message', receive);
        }
    }
    window.addEventListener('message', receive);
};

function save(data, filename) {
    try {
        if (mxClient.IS_QUIRKS || document.documentMode >= 8) {
            var win = window.open('about:blank', '_blank');
            win.document.write(data);
            win.document.close();
            win.document.execCommand('SaveAs', true, filename);
            win.close();
        }
        else if (mxClient.IS_SF) {
            // Opens new tab (user saves file). No workaround to force dialog in Safari.
            window.open('data:application/octet-stream,' + encodeURIComponent(data), filename);
        }
        else {
            var a = document.createElement('a');

            // NOTE: URL.revokeObjectURL(a.href) after click breaks download in FF
            a.href = URL.createObjectURL(new Blob([data], {type: 'application/octet-stream'}));
            a.download = filename;
            document.body.appendChild(a);
            a.click();
            a.parentNode.removeChild(a);
            URL.revokeObjectURL(a.href);
        }
    }
    catch (e) {
        console.log('error', e);
        console.log('html', data);
    }
};