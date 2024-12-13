import tensorflow as tf
from transformers import AdamWeightDecay
from model_architecture import create_model
from utils import load_dataset, split_dataset, load_tokenizer

# File paths
dataset_path = "data/kuhp_qa_dataset.csv"
tokenizer_dir = "data/tokenizer/"
model_save_dir = "data/saved_model/"

# Load Dataset
print("Loading dataset...")
df = load_dataset(dataset_path)
tokenizer = load_tokenizer(tokenizer_dir)
train_dataset, val_dataset = split_dataset(df, tokenizer_dir)

# Initialize Model
print("Initializing model...")
model = create_model()

# Optimizer and Metrics
optimizer = AdamWeightDecay(learning_rate=3e-5, weight_decay_rate=0.01)
train_accuracy_start = tf.keras.metrics.SparseCategoricalAccuracy(name="train_accuracy_start")
train_accuracy_end = tf.keras.metrics.SparseCategoricalAccuracy(name="train_accuracy_end")

# Training Loop
epochs = 3
steps_per_epoch = 300
train_losses = []

@tf.function
def train_step(batch):
    inputs, labels = batch
    with tf.GradientTape() as tape:
        outputs = model(
            input_ids=inputs["input_ids"],
            attention_mask=inputs["attention_mask"],
            token_type_ids=inputs["token_type_ids"],
            start_positions=labels["start_positions"],
            end_positions=labels["end_positions"],
            training=True
        )
        loss = outputs.loss
    gradients = tape.gradient(loss, model.trainable_variables)
    optimizer.apply_gradients(zip(gradients, model.trainable_variables))
    train_accuracy_start.update_state(labels["start_positions"], outputs.start_logits)
    train_accuracy_end.update_state(labels["end_positions"], outputs.end_logits)
    return loss

print("Starting training loop...")
for epoch in range(epochs):
    print(f"Epoch {epoch + 1}/{epochs}")
    train_accuracy_start.reset_states()
    train_accuracy_end.reset_states()

    epoch_loss = 0
    for step, batch in enumerate(train_dataset.take(steps_per_epoch)):
        loss = train_step(batch)
        epoch_loss += loss.numpy()
        if step % 10 == 0:
            print(f"Step {step + 1}/{steps_per_epoch}: Loss = {loss.numpy()}")

    train_losses.append(epoch_loss / steps_per_epoch)
    print(f"Epoch {epoch + 1} Loss: {epoch_loss / steps_per_epoch}")
    print(f"Train Accuracy Start: {train_accuracy_start.result().numpy()}, Train Accuracy End: {train_accuracy_end.result().numpy()}")

# Save Model and Tokenizer
print(f"Saving model to {model_save_dir}")
model.save_pretrained(model_save_dir)
tokenizer.save_pretrained(model_save_dir)
print("Training complete and model saved.")
