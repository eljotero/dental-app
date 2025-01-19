import { toast } from 'vue3-toastify';

export const handleSubmit = async (values: any, originalData: any, updateFunction: (id: number, dto: any) => Promise<any>, selectedId: number, successMessage: string, errorMessage: string, t: (key: string) => string) => {
    const dto: any = {};
    for (const key in values) {
        if (values[key] !== originalData[key]) {
            dto[key] = values[key];
        }
    }
    console.log(dto);
    if (Object.keys(dto).length === 0) {
        toast.info(t('noDataToUpdate'), {
            autoClose: 2000,
        });
        return;
    }
    try {
        const response = await updateFunction(selectedId, dto);
        if (response.status === 200) {
            toast.success(successMessage, {
                autoClose: 2000,
            });
            setTimeout(() => {
                window.location.reload();
            }, 2000);
        } else {
            toast.error(errorMessage, {
                autoClose: 3000,
            });
        }
    } catch (error: any) {
        console.error(error);
    }
};

export const handleRequest = async (requestFunction: (arg: any) => Promise<any>, arg: any, successMessage: string, errorMessage: string, t: (key: string) => string, targetCode: number) => {
    try {
        const response = await requestFunction(arg);
        if (response.status === targetCode) {
            toast.success(successMessage, {
                autoClose: 3000,
            });
            setTimeout(() => {
                window.location.reload();
            }, 3000);
        }
    } catch (error: any) {
        const errorMessage = error.response.data.message;
        const errorMessages = Array.isArray(errorMessage) ? errorMessage.join(', ') : errorMessage;
        toast.error(errorMessages, {
            autoClose: 3000,
        });
    }
};