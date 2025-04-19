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

/**
 * Kraken 相關工具方法
 */
const KrakenUtils = {
    getStateText: function(state) {
        switch(parseInt(state)) {
            case 0:
                return '庫存';
            case 1:
                return '運行中';
            case 2:
                return '斷線';
            default:
                return '未知狀態';
        }
    },
    
    getStateOptions: function() {
        return [
            {value: 0, text: '庫存'},
            {value: 1, text: '運行中'},
            {value: 2, text: '斷線'}
        ];
    }
};


/**
 * 初始化 Select2
 * @param {jQuery} $element - Select2 元素
 * @param {Object} options - Select2 配置選項
 * @param {string} options.placeholder - 預設提示文字
 * @param {boolean} options.multiple - 是否允許多選
 * @param {boolean} options.allowClear - 是否允許清除選擇
 * @param {string} options.width - 寬度設置
 * @param {string} options.dropdownCssClass - 下拉選單的 CSS 類名
 */
function initializeSelect2($element, options = {}) {
    const defaultOptions = {
        placeholder: '請選擇',
        multiple: false,
        allowClear: true,
        width: '100%',
        language: {
            noResults: function() {
                return '沒有符合的選項';
            }
        }
    };

    const finalOptions = { ...defaultOptions, ...options };
    
    $element.select2(finalOptions).on('select2:open', function() {
        setTimeout(function() {
            $('.select2-results__options').css('max-height', '200px');
        }, 0);
    });
    
    console.log(`${finalOptions.placeholder} Select2 初始化完成`);
}

/**
 * 更新 Select2 選項
 * @param {jQuery} $select - Select2 元素
 * @param {Map|Array} data - 選項數據
 * @param {boolean} skipEmpty - 是否跳過添加空選項
 */
function updateSelect2Options($select, data, skipEmpty = false) {
    $select.empty();
    
    // 如果不是多選且未設置跳過空選項，則添加預設的空選項
    if (!$select.prop('multiple') && !skipEmpty) {
        $select.append(new Option('', '', false, false));
    }

    let options;
    if (data instanceof Map) {
        options = Array.from(data.values());
    } else if (Array.isArray(data)) {
        options = data;
    } else {
        console.error('不支援的數據格式');
        return;
    }

    options.sort((a, b) => (a.text || a.name).localeCompare(b.text || b.name))
           .forEach(option => {
               const text = option.text || option.name;
               const value = option.id || option.value;
               $select.append(new Option(text, value, false, false));
           });

    $select.trigger('change');
    console.log(`更新 ${$select.attr('id')} 選項數量：`, options.length);
}
