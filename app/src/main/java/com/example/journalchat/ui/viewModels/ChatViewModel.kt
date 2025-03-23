package com.example.journalchat.ui.viewModels

import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journalchat.data.models.Message
import com.example.journalchat.data.models.Tag
import com.example.journalchat.data.repositories.ChatRepository
import com.example.journalchat.data.repositories.MessageRepository
import com.example.journalchat.data.repositories.TagRepository
import com.example.journalchat.ui.states.ChatMode
import com.example.journalchat.ui.states.ChatState
import com.example.journalchat.ui.uiModels.MessageUi
import com.example.journalchat.ui.uiModels.TagUi
import com.example.journalchat.ui.uiModels.toChatUi
import com.example.journalchat.ui.uiModels.toMessage
import com.example.journalchat.ui.uiModels.toMessageUi
import com.example.journalchat.ui.uiModels.toTagUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChatViewModel(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val tagRepository: TagRepository
) : ViewModel() {

    private val id: Long = checkNotNull(savedStateHandle["chatId"])

    val chat = chatRepository.getStream(id)

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private var _userTags = mutableSetOf<TagUi>()
    val userTags: List<TagUi>
        get() = _userTags.toList()

    init {
        viewModelScope.launch {
            val chatFlow = chatRepository.getStream(id).filterNotNull()
            val messageFlow = messageRepository.getAllStream(id).filterNotNull()
            combine(chatFlow, messageFlow) { chat, messages ->
                _userTags = mutableSetOf()
                ChatState(
                    chat = chat.toChatUi(),
                    messages = messages.map { message ->
                        var ref: Message? = null
                        var tag: Tag? = null
                        if (message.referenceId != null) {
                            ref = messages.find { it.id == message.referenceId }
                        }
                        if (message.tagId != null) {
                            tag = tagRepository.getStream(message.tagId).first()
                            if (tag != null) {
                                _userTags.add(tag.toTagUi())
                            }
                        }
                        message.toMessageUi(ref?.toMessageUi(), tag?.toTagUi())
                    },
                    tags = tagRepository.getAllStream().filterNotNull().first()
                        .map { it.toTagUi() })
            }.collect { _chatState.value = it }

        }
    }


    fun selectTag(tagUi: TagUi){
        var selectedTag: TagUi? = null
        if (chatState.value.filteringTag != tagUi){
            selectedTag = tagUi
        }
        _chatState.update { currentState -> currentState.copy(filteringTag = selectedTag) }
    }

    fun sendMessage() {
        val text = chatState.value.input.text.trim()

        if (text == "") return
        viewModelScope.launch {
            messageRepository.insertItem(
                Message(
                    0,
                    id,
                    null,
                    null,
                    text,
                    true,
                    LocalDateTime.now()
                )
            )
//            inputChanged("")
        }
    }

    fun inputChanged(input: TextFieldValue) {
        _chatState.update { currentState -> currentState.copy(input = input) }
    }

    fun searchInputChanged(input: TextFieldValue) {
        _chatState.update { currentState -> currentState.copy(searchInput = input) }
    }

    fun getSelectedCount(): Int {
        return _chatState.value.selectedMessages.size
    }

    fun selectMessage(messageUi: MessageUi) {
        messageUi.isSelected = !messageUi.isSelected
        if (messageUi.isSelected) {
            _chatState.update { currentState ->
                currentState.copy(
                    selectedMessages = currentState.selectedMessages.plus(
                        messageUi
                    ),
                    mode = ChatMode.Selecting
                )
            }
        } else _chatState.update { currentState ->
            currentState.copy(
                selectedMessages = currentState.selectedMessages.minus(
                    messageUi
                )
            )
        }
        if (chatState.value.selectedMessages.isEmpty()) {
            switchMode(ChatMode.Chatting)
        }
    }


    fun clearSelection() {
        _chatState.value.selectedMessages.map { it.isSelected = false }
        _chatState.update { currentState ->
            currentState.copy(selectedMessages = listOf(), mode = ChatMode.Chatting)
        }
    }

    fun stopAction() {
        clearSelection()
        inputChanged(TextFieldValue())
    }


    fun switchMode(chatMode: ChatMode) {
        _chatState.update { currentState ->
            currentState.copy(mode = chatMode)
        }
    }

    fun deleteMessages() {
        viewModelScope.launch {
            messageRepository.deleteMessages(chatState.value.selectedMessages.map { it.toMessage() })
        }
    }

    fun startEditing() {
        if (chatState.value.selectedMessages.size != 1) return
        val message = chatState.value.selectedMessages[0]
        inputChanged(TextFieldValue(message.content, TextRange(message.content.length)))
        switchMode(ChatMode.Editing)
    }

    fun startReplying() {
        if (chatState.value.selectedMessages.size != 1) return
        inputChanged(TextFieldValue())
        switchMode(ChatMode.Replying)
    }


    fun editMessageText() {
        if (chatState.value.selectedMessages.size != 1) {
            Log.e("editMessage", "Trying editing message while selecting list size is not one")
            return;
        }

        val text = chatState.value.input.text.trim()
        if (text == "") return

        val updatedMessage =
            chatState.value.selectedMessages[0].copy(content = text)

        viewModelScope.launch {
            messageRepository.updateItem(
                updatedMessage.toMessage()
            )
        }
    }

    fun replyMessage() {
        if (chatState.value.selectedMessages.size != 1) {
            Log.e("replyMessage", "Trying reply on message while selecting list size is not one")
            return;
        }

        if (chatState.value.input.text == "") return

        val selectedMessage = chatState.value.selectedMessages[0]

        val newMessage = Message(
            0,
            id,
            null,
            selectedMessage.id,
            chatState.value.input.text,
            true,
            LocalDateTime.now()
        )

        viewModelScope.launch {
            messageRepository.insertItem(
                newMessage
            )
        }
    }

    fun applyTag(tagUi: TagUi?) {
        if (chatState.value.selectedMessages.size != 1) {
            Log.e("Custom", "Applying tag, but selected messages size is not one!")
            return;
        }
        var message = chatState.value.selectedMessages[0]
        message = message.copy(tagId = tagUi?.id)
        Log.i("custom", message.toMessage().toString())

        viewModelScope.launch {
            messageRepository.updateItem(
                message.toMessage()
            )
        }
    }

    fun applyTag(message: MessageUi, tagUi: TagUi?) {
        val updatedMessage = message.copy(tagId = tagUi?.id)
        Log.i("custom", updatedMessage.toMessage().toString())

        viewModelScope.launch {
            messageRepository.updateItem(
                updatedMessage.toMessage()
            )
        }


    }



}




