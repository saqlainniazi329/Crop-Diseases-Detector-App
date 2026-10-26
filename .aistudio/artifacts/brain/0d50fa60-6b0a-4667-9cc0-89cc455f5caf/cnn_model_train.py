import os
import tensorflow as tf
from tensorflow.keras import layers, models

# =====================================================================
# CROP DISEASE DETECTION CNN MODEL TRAINING SCRIPT
# BS Information Technology Final Year Project (IUB)
# Developer: Saqlain Niazi
# =====================================================================

IMAGE_SIZE = (224, 224)
BATCH_SIZE = 32
CHANNELS = 3
EPOCHS = 20
NUM_CLASSES = 4
CLASS_NAMES = ["Bacterial Blight", "Blast", "Brown Spot", "Healthy"]

def build_crop_cnn_model():
    """
    Builds the Sequential Convolutional Neural Network (CNN) architecture:
    - 3 Conv2D layers (filters: 32, 64, 64 with 'relu' activation)
    - Interleaved with MaxPooling2D layers
    - Flatten layer
    - Dense feature layer (64 units, relu activation)
    - Output decision layer (4 units, softmax activation)
    """
    model = models.Sequential([
        # Input and First Convolutional Block
        layers.Input(shape=(IMAGE_SIZE[0], IMAGE_SIZE[1], CHANNELS)),
        layers.Conv2D(32, (3, 3), activation='relu', name="conv_layer_1"),
        layers.MaxPooling2D((2, 2), name="maxpool_1"),

        # Second Convolutional Block
        layers.Conv2D(64, (3, 3), activation='relu', name="conv_layer_2"),
        layers.MaxPooling2D((2, 2), name="maxpool_2"),

        # Third Convolutional Block
        layers.Conv2D(64, (3, 3), activation='relu', name="conv_layer_3"),
        layers.MaxPooling2D((2, 2), name="maxpool_3"),

        # Flatten & Dense Classification Head
        layers.Flatten(name="flatten"),
        layers.Dense(64, activation='relu', name="dense_64"),
        layers.Dense(NUM_CLASSES, activation='softmax', name="output_decision_layer")
    ])

    return model

def compile_and_train():
    print("==================================================================")
    print(" AI CROP DISEASE DETECTION - CNN MODEL TRAINING")
    print(" Islamia University of Bahawalpur (IUB) - BS IT FYP")
    print(" Developer: Saqlain Niazi")
    print(" Target Classes:", CLASS_NAMES)
    print("==================================================================")

    # Instantiate Sequential CNN model
    model = build_crop_cnn_model()

    # Compile network parameters using 'adam' optimizer and 'SparseCategoricalCrossentropy' loss
    model.compile(
        optimizer='adam',
        loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=False),
        metrics=['accuracy']
    )

    # Print model architecture summary
    model.summary()

    # Optional Dataset Loading Pipeline
    dataset_dir = os.path.join(os.path.dirname(__file__), "dataset")
    if os.path.exists(dataset_dir):
        print(f"[*] Found training dataset at '{dataset_dir}'. Preparing tf.data pipelines...")
        train_ds = tf.keras.utils.image_dataset_from_directory(
            dataset_dir,
            validation_split=0.2,
            subset="training",
            seed=42,
            image_size=IMAGE_SIZE,
            batch_size=BATCH_SIZE
        )
        val_ds = tf.keras.utils.image_dataset_from_directory(
            dataset_dir,
            validation_split=0.2,
            subset="validation",
            seed=42,
            image_size=IMAGE_SIZE,
            batch_size=BATCH_SIZE
        )

        normalization_layer = layers.Rescaling(1./255)
        train_ds = train_ds.map(lambda x, y: (normalization_layer(x), y)).cache().prefetch(buffer_size=tf.data.AUTOTUNE)
        val_ds = val_ds.map(lambda x, y: (normalization_layer(x), y)).cache().prefetch(buffer_size=tf.data.AUTOTUNE)

        print("[*] Training CNN network...")
        model.fit(
            train_ds,
            validation_data=val_ds,
            epochs=EPOCHS
        )
    else:
        print(f"[*] Note: '{dataset_dir}' directory not found. Initializing architecture...")

    # Operational method statement to save trained model binary
    output_filename = "crop_disease_model.h5"
    model.save(output_filename)
    print(f"\n[+] Successfully compiled and saved trained model binary to '{output_filename}'.")

if __name__ == "__main__":
    compile_and_train()
