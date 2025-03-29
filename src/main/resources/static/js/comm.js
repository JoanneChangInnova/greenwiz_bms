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


/**
 * 根據設備類型更新設備型號和功能模式選項
 * @param {Object} form - layui form 對象
 * @param {string} deviceType - 設備類型
 */
function updateDeviceModelOptions(form, deviceType) {
    var $ = layui.$;
    var deviceModelSelect = $('#deviceModel');
    var functionModeSelect = $('#functionMode');
    
    // 重置功能模式
    $('#functionModeContainer').hide();
    functionModeSelect.empty().append('<option value="">請選擇功能模式</option>');
    
    // 重置設備型號
    deviceModelSelect.empty();
    deviceModelSelect.append('<option value="">請選擇設備型號</option>');

    if (deviceType === 'CONTROLLER') {
        deviceModelSelect.append('<option value="SWB">SWB</option>');
        deviceModelSelect.append('<option value="CX-IR0001S">CX-IR0001S</option>');
        deviceModelSelect.append('<option value="AMA-Fans">AMA-Fans</option>');
    } else if (deviceType === 'MONITOR') {
        deviceModelSelect.append('<option value="METSEPM2220">METSEPM2220</option>');
        deviceModelSelect.append('<option value="METSEPM1125HCL05RD">METSEPM1125HCL05RD</option>');
        deviceModelSelect.append('<option value="IEM2455-230V-100A">IEM2455-230V-100A</option>');
        
        // Monitor 類型時設置功能模式選項
        $('#functionModeContainer').show();
        functionModeSelect.append('<option value="1P+N">1P+N</option>');
        functionModeSelect.append('<option value="2P">2P</option>');
        functionModeSelect.append('<option value="2P+N">2P+N</option>');
        functionModeSelect.append('<option value="3P">3P</option>');
        functionModeSelect.append('<option value="3P+N">3P+N</option>');
    }
    
    form.render('select');
}


/**
 * 驗證通道代號格式
 * @param {string} channelName - 通道代號
 * @returns {boolean} - 驗證結果
 */
function validateChannelName(channelName) {
    var pattern = /^[A-Z](-(M|\d{2}))?(-\d{2}){0,2}$/;
    return pattern.test(channelName);
}

/**
 * 重置功能模式選項
 */
function resetFunctionMode() {
    $('#functionModeContainer').hide();
    $('#functionMode, #functionModeSelect').empty().append('<option value="">請選擇功能模式</option>');
}
