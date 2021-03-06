package data;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import entities.Question;
import entities.Quiz;

@Transactional
public class QuizDAOImpl implements QuizDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Quiz> index() {
		String query = "SELECT quiz FROM Quiz quiz";
		return em.createQuery(query, Quiz.class).getResultList();
	}

	@Override
	public Quiz show(int id) {
		return em.find(Quiz.class, id);
	}

	@Override
	public Quiz create(Quiz quiz) {
		em.persist(quiz);
		em.flush();
		return quiz;
	}

	@Override
	public Quiz update(int id, Quiz quiz) {
		Quiz managedQuiz = em.find(Quiz.class, id);
		managedQuiz.setName(quiz.getName());
		
		return managedQuiz;
	}

	@Override
	public boolean destroy(int id) {
		boolean flag = false;
		try {			
			em.remove(em.find(Quiz.class, id));
			flag = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return flag;
	}

	@Override
	public Set<Question> showQuestions(int quizId) {
		Set<Question> questions = null;

		String queryString = "SELECT quiz " 
				+ "FROM Quiz quiz JOIN FETCH quiz.questions " 
				+ "WHERE quiz.id = :id";
		try {
			Quiz quiz = em.createQuery(queryString, Quiz.class)
			.setParameter("id", quizId)
			.getSingleResult();
			questions = quiz.getQuestions();
		} catch (Exception e) {
			System.out.println(e);
		}
		return questions;
	}

	@Override
	public Question createQuestion(int quizId, Question question) {
		Quiz quiz = em.find(Quiz.class, quizId);
		question.setQuiz(quiz);
		em.persist(question);
		em.flush();
		return question;
	}

	@Override
	public boolean destroyQuestion(int quizId, int questionId) {
		boolean flag = false;
		try {			
			em.remove(em.find(Question.class, questionId));
			flag = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return flag;
	}

}
