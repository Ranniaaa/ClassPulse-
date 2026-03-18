package anonymous;

import java.util.ArrayList;

public class AnonymousQuestionManager {

    // قائمة لتخزين جميع الأسئلة
    private ArrayList<AnonymousQuestion> questions;

    // Constructor
    public AnonymousQuestionManager() {
        questions = new ArrayList<>();
    }

    // إضافة سؤال جديد
    public void addQuestion(int id, String content) {
        AnonymousQuestion newQuestion = new AnonymousQuestion(id, content);
        questions.add(newQuestion);
    }

    // إرجاع جميع الأسئلة
    public ArrayList<AnonymousQuestion> getQuestions() {
        return questions;
    }

    // البحث عن سؤال حسب id
    public AnonymousQuestion findQuestionById(int id) {
        for (AnonymousQuestion q : questions) {
            if (q.getId() == id) {
                return q;
            }
        }
        return null; // إذا لم يتم العثور عليه
    }

    // عمل upvote لسؤال معين
    public void upvoteQuestion(int id) {
        AnonymousQuestion q = findQuestionById(id);
        if (q != null) {
            q.upvote();
        }
    }

    // تحديد سؤال كمجاب عنه
    public void markQuestionAsAnswered(int id) {
        AnonymousQuestion q = findQuestionById(id);
        if (q != null) {
            q.markAsAnswered();
        }
    }
}