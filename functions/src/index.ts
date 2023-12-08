import {
    onCall,
    HttpsError,
    CallableRequest,
} from "firebase-functions/v2/https";
import * as admin from "firebase-admin";
import {info, error, debug} from "firebase-functions/logger";

admin.initializeApp();

interface NotificationData {
    title: string;
    body: string;
    tokens: string[];
}

const signature = "DEBUG: sendNotification:"

console.log(signature, "Initializing function")

export const sendNotification =
    onCall((request: CallableRequest<NotificationData>) => {
        console.log(signature, "Request received", request);
        debug("Request received", request);
        // if (!request.auth) {
        //     console.log(signature, "Request not authenticated")
        //     throw new HttpsError(
        //         "unauthenticated",
        //         "The function must be called while authenticated."
        //     );
        // }

        console.log(signature, "Request authenticated")
        debug("Request authenticated")

        const data = request.data;
        console.log(signature, "Data received", data);
        debug("Data received", data);
        const messages = data.tokens.map((token) => ({
            notification: {
                title: data.title,
                body: data.body,
            },
            token: token,
        }));

        debug("Messages to send", messages)

        return admin.messaging().sendEach(messages)
            .then((response) => {
                info("Notification sent");
                return {success: true, response: response.responses};
            })
            .catch((e) => {
                error("Notification not sent", e.message)
                throw new HttpsError("unknown", e.message, e);
            });
    });