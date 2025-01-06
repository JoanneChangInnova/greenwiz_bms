import en from './en.json';
import zh from './zh-tw.json';

const i18n = {
	en,
	zh
};

// 獲取當前語言，默認為中文
let currentLang = localStorage.getItem('lang') || 'en';

// 切換語言
export function setLanguage(lang) {
	if (i18n[lang]) {
		currentLang = lang;
		localStorage.setItem('lang', lang);
	}
}

// 獲取當前語言包
export function t(key) {
	const keys = key.split('.');
	return keys.reduce((obj, k) => (obj && obj[k] ? obj[k] : key), i18n[currentLang]);
}
