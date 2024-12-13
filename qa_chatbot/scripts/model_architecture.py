from transformers import TFBertForQuestionAnswering

def create_model():
    """
    Initialize and return a BERT-based model for Question Answering.
    """
    model = TFBertForQuestionAnswering.from_pretrained("bert-base-multilingual-cased")
    # Apply dropout regularization
    model.config.hidden_dropout_prob = 0.3
    model.config.attention_probs_dropout_prob = 0.3
    return model
