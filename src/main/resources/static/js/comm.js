function handleErrorResponse(xhr) {
	try {
		var response = xhr.responseJSON || JSON.parse(xhr.responseText);
		console.error("解析 response:", response);

		// 處理錯誤訊息
		var errorMessage = "發生未知錯誤";
		if (Array.isArray(response)) {
			errorMessage = response.join("<br>");  // 處理多條錯誤訊息
		} else if (response.message) {
			errorMessage = response.message; // 只顯示錯誤訊息，不顯示完整 JSON
		} else if (response.msg) {
			errorMessage = response.msg;
		}

		// 顯示錯誤訊息彈窗
		layer.alert(errorMessage, { title: '錯誤', icon: 2 }, function(index) {
			if (response.code === 'VERSION_INVALID') {
				// 若版本錯誤，則回上一頁並刷新
				window.history.back();
				location.reload();
			}

			// 若為 400 或 401 錯誤，刷新驗證碼並清空輸入框
			if (xhr.status === 400 || xhr.status === 401) {
				console.log("刷新驗證碼");
				if (typeof refreshCaptcha === 'function') {
					refreshCaptcha();
				}

				setTimeout(function() {
					var captchaInput = $('#captchaInput');
					if (captchaInput.length) {
						console.log("清空驗證碼輸入欄位");
						captchaInput.val('');
					}
				}, 100); // 確保 DOM 更新完成
			}

			// 關閉彈窗
			layer.close(index);
		});

	} catch (e) {
		console.error("解析錯誤回應失敗:", e);
		layer.alert('伺服器錯誤，請稍後再試', { title: '錯誤', icon: 2 });
	}
}
