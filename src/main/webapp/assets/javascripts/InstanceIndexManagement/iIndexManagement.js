(function() {
	$(document).ready(function() {
		$("#openLengthIndex").click(function () {
			$.ajax({
				cache : true,
				type : "GET",
				url : 'instanceIndex/index_open',
				data : {
					indexName : "length"
				},
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						document.getElementById("closeLengthIndex").removeAttribute("disabled");
						$("#openLengthIndex").attr("disabled", true);
						alert('Save Successed!!');
					} else {
						alert(data.message);
					}
				}
			});
		});
		
		$("#closeLengthIndex").click(function() {
			$.ajax({
				cache : true,
				type : "GET",
				url : 'instanceIndex/index_close',
				data : {
					indexName : "length"
				},
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						document.getElementById("openLengthIndex").removeAttribute("disabled");
						$("#closeLengthIndex").attr("disabled", true);
						alert('Close Successed!!');
					} else {
						alert(data.message);
					}
				}
			});
		});
		$("#openDurationIndex").click(function () {
			$.ajax({
				cache : true,
				type : "GET",
				url : 'instanceIndex/index_open',
				data : {
					indexName : "duration"
				},
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						document.getElementById("closeDurationIndex").removeAttribute("disabled");
						$("#openDurationIndex").attr("disabled", true);
						alert('Save Successed!!');
					} else {
						alert(data.message);
					}
				}
			});
		});
		
		$("#closeDurationIndex").click(function() {
			$.ajax({
				cache : true,
				type : "GET",
				url : 'instanceIndex/index_close',
				data : {
					indexName : "duration"
				},
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						document.getElementById("openDurationIndex").removeAttribute("disabled");
						$("#closeDurationIndex").attr("disabled", true);
						alert('Close Successed!!');
					} else {
						alert(data.message);
					}
				}
			});
		});
		$("#openEventIndex").click(function () {
			$.ajax({
				cache : true,
				type : "GET",
				url : 'instanceIndex/index_open',
				data : {
					indexName : "event"
				},
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						document.getElementById("closeEventIndex").removeAttribute("disabled");
						$("#openEventIndex").attr("disabled", true);
						alert('Save Successed!!');
					} else {
						alert(data.message);
					}
				}
			});
		});
		
		$("#closeEventIndex").click(function() {
			$.ajax({
				cache : true,
				type : "GET",
				url : 'instanceIndex/index_close',
				data : {
					indexName : "event"
				},
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						document.getElementById("openEventIndex").removeAttribute("disabled");
						$("#closeEventIndex").attr("disabled", true);
						alert('Close Successed!!');
					} else {
						alert(data.message);
					}
				}
			});
		});
		$("#openAEventIndex").click(function () {
			$.ajax({
				cache : true,
				type : "GET",
				url : 'instanceIndex/index_open',
				data : {
					indexName : "aevent"
				},
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						document.getElementById("closeAEventIndex").removeAttribute("disabled");
						$("#openAEventIndex").attr("disabled", true);
						alert('Save Successed!!');
					} else {
						alert(data.message);
					}
				}
			});
		});
		
		$("#closeAEventIndex").click(function() {
			$.ajax({
				cache : true,
				type : "GET",
				url : 'instanceIndex/index_close',
				data : {
					indexName : "aevent"
				},
				async : true,
				error : function(request) {
					alert("Connection error");
				},
				success : function(data) {
					if (data.state == "SUCCESS") {
						document.getElementById("openAEventIndex").removeAttribute("disabled");
						$("#closeAEventIndex").attr("disabled", true);
						alert('Close Successed!!');
					} else {
						alert(data.message);
					}
				}
			});
		});
	});
}).call(this);
