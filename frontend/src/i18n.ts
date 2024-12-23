import { createI18n } from 'vue-i18n';
import en from './locale/en.json';
import pl from './locale/pl.json';

const i18n = createI18n({
    legacy: false,
    locale: localStorage.getItem('lang') || 'en',
    fallbackLocale: 'en',
    messages: {
        en,
        pl,
    },
});

export default i18n;