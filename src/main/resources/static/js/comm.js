function handleErrorResponse(xhr) {
	try {
		var response = JSON.parse(xhr.responseText);
		console.error("解析response:", response);

		// 處理 response 是字串陣列的情況
		var errorMessage = "";
		if (Array.isArray(response)) {
			// 如果是陣列，將每個錯誤訊息拼接成一個字串
			errorMessage = response.join("<br>");  // 使用 <br> 換行顯示每個錯誤
		} else if (response.msg) {
			// 如果是物件，顯示 msg 欄位
			errorMessage = response.msg;
		} else {
			// 如果沒有 msg，顯示其他錯誤訊息或原始內容
			errorMessage = xhr.responseText;
		}

		// 顯示錯誤訊息
		layer.alert('提交失敗: ' + errorMessage, {title: '錯誤'}, function(index) {
			if (response.code === 'VERSION_INVALID') {
				// 在關閉提示框後回上一頁並重新加載
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
