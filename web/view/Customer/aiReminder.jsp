<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- AI Chat Box Component - Có thể nhúng vào bất kỳ trang nào -->
<div class="ai-chat-widget">
    <style>
        .ai-chat-widget {
            width: 380px;
            max-width: 100%;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.15);
            overflow: hidden;
            display: flex;
            flex-direction: column;
            height: 500px;
            font-family: 'Segoe UI', Roboto, Arial, sans-serif;
        }

        .ai-chat-widget * {
            box-sizing: border-box;
        }

        /* Header */
        .ai-chat-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px;
            display: flex;
            align-items: center;
            gap: 10px;
            flex-shrink: 0;
        }

        .ai-chat-header i {
            font-size: 18px;
        }

        .ai-chat-header h3 {
            margin: 0;
            font-size: 16px;
            font-weight: 600;
        }

        /* Car Selection */
        .ai-car-select-wrapper {
            padding: 12px;
            background: #f8f9fa;
            border-bottom: 1px solid #e5e7eb;
            flex-shrink: 0;
        }

        .ai-car-select-wrapper label {
            display: block;
            font-size: 12px;
            color: #666;
            margin-bottom: 6px;
            font-weight: 600;
        }

        .ai-car-select {
            width: 100%;
            padding: 8px 10px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 13px;
            background: white;
            cursor: pointer;
        }

        .ai-car-select:focus {
            outline: none;
            border-color: #667eea;
        }

        /* Messages Area */
        .ai-chat-messages {
            flex: 1;
            padding: 15px;
            overflow-y: auto;
            background: #f9fafb;
            min-height: 0;
        }

        /* Custom Scrollbar */
        .ai-chat-messages::-webkit-scrollbar {
            width: 6px;
        }

        .ai-chat-messages::-webkit-scrollbar-track {
            background: #f1f1f1;
        }

        .ai-chat-messages::-webkit-scrollbar-thumb {
            background: #667eea;
            border-radius: 3px;
        }

        .ai-chat-messages::-webkit-scrollbar-thumb:hover {
            background: #5568d3;
        }

        /* Message Bubbles */
        .ai-message {
            margin-bottom: 12px;
            display: flex;
            gap: 8px;
            animation: slideIn 0.3s ease;
        }

        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .ai-message.user {
            justify-content: flex-end;
        }

        .ai-message-bubble {
            max-width: 75%;
            padding: 10px 14px;
            border-radius: 12px;
            line-height: 1.5;
            font-size: 14px;
            word-wrap: break-word;
        }

        .ai-message.user .ai-message-bubble {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 12px 12px 0 12px;
        }

        .ai-message.ai .ai-message-bubble {
            background: white;
            color: #333;
            border: 1px solid #e5e7eb;
            border-radius: 12px 12px 12px 0;
            box-shadow: 0 1px 2px rgba(0,0,0,0.05);
        }

        /* Quick Actions */
        .ai-quick-actions {
            padding: 10px 12px;
            background: white;
            border-top: 1px solid #e5e7eb;
            display: flex;
            gap: 6px;
            flex-wrap: wrap;
            flex-shrink: 0;
        }

        .ai-quick-btn {
            padding: 6px 12px;
            background: #f3f4f6;
            border: 1px solid #e5e7eb;
            border-radius: 16px;
            font-size: 12px;
            cursor: pointer;
            transition: all 0.2s;
            white-space: nowrap;
        }

        .ai-quick-btn:hover {
            background: #667eea;
            color: white;
            border-color: #667eea;
        }

        /* Input Area */
        .ai-chat-input-area {
            padding: 12px;
            background: white;
            border-top: 1px solid #e5e7eb;
            flex-shrink: 0;
        }

        .ai-chat-form {
            display: flex;
            gap: 8px;
        }

        .ai-chat-input {
            flex: 1;
            padding: 10px 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 14px;
            resize: none;
            min-height: 38px;
            max-height: 80px;
        }

        .ai-chat-input:focus {
            outline: none;
            border-color: #667eea;
        }

        .ai-btn-send {
            padding: 10px 16px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s;
            font-size: 14px;
            flex-shrink: 0;
        }

        .ai-btn-send:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
        }

        .ai-btn-send:disabled {
            opacity: 0.6;
            cursor: not-allowed;
            transform: none;
        }

        /* Responsive */
        @media (max-width: 480px) {
            .ai-chat-widget {
                width: 100%;
                height: 450px;
            }
            
            .ai-quick-actions {
                overflow-x: auto;
                flex-wrap: nowrap;
            }
            
            .ai-quick-actions::-webkit-scrollbar {
                height: 4px;
            }
        }
    </style>

    <!-- Header -->
    <div class="ai-chat-header">
        <i class="fas fa-robot"></i>
        <h3>AI Tư vấn Bảo dưỡng</h3>
    </div>

    <!-- Car Selection -->
    <div class="ai-car-select-wrapper">
        <label>Chọn xe để tư vấn:</label>
        <select id="aiChatCarSelect" class="ai-car-select">
            <option value="">-- Chọn xe --</option>
            <c:forEach items="${carList}" var="car">
                <option value="${car.carId}">
                    ${car.brand} ${car.model} - ${car.licensePlate}
                </option>
            </c:forEach>
        </select>
    </div>

    <!-- Messages -->
    <div class="ai-chat-messages" id="aiChatMessages">
        <div class="ai-message ai">
            <div class="ai-message-bubble">
                👋 Xin chào! Tôi là trợ lý AI chuyên về bảo dưỡng ô tô. Chọn xe và đặt câu hỏi nhé!
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="ai-quick-actions">
        <button class="ai-quick-btn" onclick="aiSendQuick('Xe tôi cần bảo dưỡng gì?')">
            🔧 Cần bảo dưỡng gì?
        </button>
        <button class="ai-quick-btn" onclick="aiSendQuick('Bao lâu nên thay dầu?')">
            🛢️ Thay dầu?
        </button>
        <button class="ai-quick-btn" onclick="aiSendQuick('Giá bảo dưỡng?')">
            💰 Chi phí?
        </button>
    </div>

    <!-- Input Area -->
    <div class="ai-chat-input-area">
        <form class="ai-chat-form" id="aiChatForm" onsubmit="aiSendMessage(event)">
            <input type="text" 
                   class="ai-chat-input" 
                   id="aiChatInput" 
                   placeholder="Nhập câu hỏi..." 
                   required>
            <button type="submit" class="ai-btn-send" id="aiSendBtn">
                <i class="fas fa-paper-plane"></i>
            </button>
        </form>
    </div>
</div>

<script>
    // AI Chat Widget Functions
    function aiSendMessage(event) {
        event.preventDefault();
        const input = document.getElementById('aiChatInput');
        const message = input.value.trim();
        const carId = document.getElementById('aiChatCarSelect').value;

        if (!carId) {
            alert("❌ Vui lòng chọn xe trước!");
            return;
        }

        if (message) {
            aiAddMessage(message, 'user');
            input.value = '';
            aiCallAPI(message, carId);
        }
    }

    function aiSendQuick(message) {
        const carId = document.getElementById('aiChatCarSelect').value;
        if (!carId) {
            alert("❌ Vui lòng chọn xe trước!");
            return;
        }
        aiAddMessage(message, 'user');
        aiCallAPI(message, carId);
    }

    function aiAddMessage(text, sender) {
        const messagesDiv = document.getElementById('aiChatMessages');
        const messageDiv = document.createElement('div');
        messageDiv.className = 'ai-message ' + sender;

        const bubble = document.createElement('div');
        bubble.className = 'ai-message-bubble';
        bubble.textContent = text;

        messageDiv.appendChild(bubble);
        messagesDiv.appendChild(messageDiv);
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    }

    function aiCallAPI(message, carId) {
        const sendBtn = document.getElementById('aiSendBtn');
        sendBtn.disabled = true;
        sendBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';

        const typingId = 'typing-' + Date.now();
        const messagesDiv = document.getElementById('aiChatMessages');
        const typingDiv = document.createElement('div');
        typingDiv.id = typingId;
        typingDiv.className = 'ai-message ai';
        typingDiv.innerHTML = '<div class="ai-message-bubble">💭 Đang suy nghĩ...</div>';
        messagesDiv.appendChild(typingDiv);
        messagesDiv.scrollTop = messagesDiv.scrollHeight;

        fetch('${pageContext.request.contextPath}/freeAiReminder', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'action=chat&message=' + encodeURIComponent(message) + '&carId=' + encodeURIComponent(carId)
        })
        .then(res => res.json())
        .then(data => {
            document.getElementById(typingId).remove();
            const response = data.error ? '❌ Lỗi: ' + data.error : (data.response || 'Xin lỗi, không thể trả lời.');
            aiAddMessage(response, 'ai');
        })
        .catch(err => {
            document.getElementById(typingId).remove();
            aiAddMessage('❌ Lỗi kết nối: ' + err.message, 'ai');
        })
        .finally(() => {
            sendBtn.disabled = false;
            sendBtn.innerHTML = '<i class="fas fa-paper-plane"></i>';
        });
    }
</script>