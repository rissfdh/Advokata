import pandas as pd
import tensorflow as tf
from transformers import BertTokenizerFast

def prepare_dataset(df, tokenizer, max_length=512):
    """
    Converts the dataset into a TensorFlow dataset for training and validation.

    Args:
        df (pd.DataFrame): The DataFrame containing the question and context (jawaban).
        tokenizer (transformers.BertTokenizerFast): The tokenizer to tokenize the input.
        max_length (int): Maximum sequence length for tokenization.

    Returns:
        tf.data.Dataset: TensorFlow dataset ready for training or validation.
    """
    input_ids, attention_masks, token_type_ids, start_positions, end_positions = [], [], [], [], []
    for _, row in df.iterrows():
        question = row['pertanyaan']
        context = row['jawaban'][7:-6]  # Remove <start> and <end>

        # Tokenize question and context
        inputs = tokenizer.encode_plus(
            question,
            context,
            add_special_tokens=True,
            max_length=max_length,
            truncation=True,
            padding="max_length",
            return_offsets_mapping=True,
            return_tensors="tf"
        )

        # Find start and end indices of the answer in the context
        start_idx = context.find(row['jawaban'][7:-6])
        end_idx = start_idx + len(row['jawaban'][7:-6])

        if start_idx == -1 or end_idx == -1:
            # Skip this data point if indices are invalid
            continue

        # Map character positions to token positions
        offsets = inputs['offset_mapping'][0].numpy()
        start_token, end_token = 0, 0
        for idx, (start, end) in enumerate(offsets):
            if start <= start_idx < end:
                start_token = idx
            if start < end_idx <= end:
                end_token = idx

        # Ensure positions are valid within max_length
        if start_token >= max_length or end_token >= max_length:
            continue

        # Append data to respective lists
        input_ids.append(inputs['input_ids'][0])
        attention_masks.append(inputs['attention_mask'][0])
        token_type_ids.append(inputs['token_type_ids'][0])
        start_positions.append(start_token)
        end_positions.append(end_token)

    # Convert to TensorFlow dataset
    return tf.data.Dataset.from_tensor_slices(({
        "input_ids": tf.convert_to_tensor(input_ids),
        "attention_mask": tf.convert_to_tensor(attention_masks),
        "token_type_ids": tf.convert_to_tensor(token_type_ids),
    }, {
        "start_positions": tf.convert_to_tensor(start_positions),
        "end_positions": tf.convert_to_tensor(end_positions),
    }))

def load_dataset(file_path):
    """
    Loads and preprocesses the dataset from a CSV file.

    Args:
        file_path (str): Path to the dataset file (CSV).

    Returns:
        pd.DataFrame: The loaded dataset with added start and end tokens in the 'jawaban' column.
    """
    df = pd.read_csv(file_path)
    df['jawaban'] = '<start> ' + df['jawaban'] + ' <end>'
    return df

def split_dataset(df, tokenizer_dir, batch_size=4, max_length=512):
    """
    Splits the dataset into training and validation datasets.

    Args:
        df (pd.DataFrame): The DataFrame containing the dataset.
        tokenizer_dir (str): Path to the tokenizer directory.
        batch_size (int): Batch size for the dataset.
        max_length (int): Maximum sequence length for tokenization.

    Returns:
        tuple: Training and validation TensorFlow datasets.
    """
    tokenizer = BertTokenizerFast.from_pretrained(tokenizer_dir)
    train_size = int(0.8 * len(df))
    train_dataset = prepare_dataset(df[:train_size], tokenizer, max_length).shuffle(100).batch(batch_size).prefetch(tf.data.AUTOTUNE)
    val_dataset = prepare_dataset(df[train_size:], tokenizer, max_length).batch(batch_size).prefetch(tf.data.AUTOTUNE)
    return train_dataset, val_dataset

def load_tokenizer(tokenizer_dir):
    """
    Loads the tokenizer from the specified directory.

    Args:
        tokenizer_dir (str): Path to the tokenizer directory.

    Returns:
        BertTokenizerFast: The loaded tokenizer.
    """
    return BertTokenizerFast.from_pretrained(tokenizer_dir)
