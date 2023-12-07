import {
    onCall,
    HttpsError,
    CallableRequest,
} from "firebase-functions/v2/https";
import * as admin from "firebase-admin";
import { info, error } from "firebase-functions/logger";

admin.initializeApp();

interface NotificationData {
  title: string;
  body: string;
  tokens: string[];
}

export const sendNotification =
  onCall((request: CallableRequest<NotificationData>) => {
    if (!request.auth) {
      throw new HttpsError(
        "unauthenticated",
        "The function must be called while authenticated."
      );
    }

    const data = request.data;
    const messages = data.tokens.map((token) => ({
      notification: {
        title: data.title,
        body: data.body,
      },
      token: token,
    }));

    return admin.messaging().sendEach(messages)
      .then((response) => {
        info("Notification sent");
        return { success: true, response: response.responses };
      })
      .catch((e) => {
        error("Notification not sent", e.message)
        throw new HttpsError("unknown", e.message, e);
      });
  });