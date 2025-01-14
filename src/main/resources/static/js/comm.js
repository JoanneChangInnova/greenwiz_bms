function handleErrorResponse(xhr) {
	try {
		var response = JSON.parse(xhr.responseText);

		layer.alert('提交失敗: ' + response.msg, {title: '錯誤'}, function(index) {
			// 在確定後回上一頁並重新加載
			if (response.code === 'VERSION_INVALID') {
				window.history.back();
				location.reload();
			}
			// 關閉提示框
			layer.close(index);
		});
	} catch (e) {
		// 如果解析 JSON 失敗，顯示通用錯誤訊息
		console.error("解析錯誤回應失敗:", e);
		layer.alert('提交失敗: 伺服器錯誤', { title: '錯誤' });
	}
}