export const responseErrorToString = async (response: Response) => {
    try {
        return await response.text();
    } catch {
        return response.status + " " + response.statusText;
    }
};
