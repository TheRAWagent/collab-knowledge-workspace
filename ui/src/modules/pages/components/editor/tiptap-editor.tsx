import {
  useEditor,
  EditorContent,
  type JSONContent,
  useEditorState
} from "@tiptap/react";
import { BubbleMenu, FloatingMenu } from '@tiptap/react/menus'
import TaskList from "@tiptap/extension-task-list";
import TaskItem from "@tiptap/extension-task-item";
import Document from "@tiptap/extension-document";
import Collaboration from "@tiptap/extension-collaboration";
import { TextStyleKit } from "@tiptap/extension-text-style";
import BoldExtension from "@tiptap/extension-bold";
import ItalicExtension from "@tiptap/extension-italic";
import StrikethroughExtension from "@tiptap/extension-strike";
import CodeExtension from "@tiptap/extension-code";
import Subscript from "@tiptap/extension-subscript";
import Superscript from "@tiptap/extension-superscript";
import Underline from "@tiptap/extension-underline";
import Heading from "@tiptap/extension-heading";
import Text from "@tiptap/extension-text";
import Blockquote from "@tiptap/extension-blockquote";
import { BulletList, ListItem, OrderedList } from '@tiptap/extension-list';
import UndoRedo from "@tiptap/extension-history";
import CodeBlock from "@tiptap/extension-code-block";
import HorizontalRule from "@tiptap/extension-horizontal-rule";
import Paragraph from "@tiptap/extension-paragraph";
import { useEffect, useMemo } from "react";
import { Button } from "@/components/ui/button";
import {
  Bold,
  Italic,
  Strikethrough,
  Code,
  Heading1,
  Heading2,
  Heading3,
  List,
  ListOrdered,
  ListTodo,
  Quote,
  Minus,
  Undo,
  Redo,
  UnderlineIcon,
} from "lucide-react";
import "@/styles/styles.css";
import { Doc } from "yjs";
import { HocuspocusProvider } from "@hocuspocus/provider";
import CollaborationCaret from "@tiptap/extension-collaboration-caret";
import { nodeWithId } from "@/lib/node-with-id";
import { useParams } from "@tanstack/react-router";
import { useGetUser } from "@/modules/users/api.ts";
import { createLowlight } from "lowlight";
import javascript from "highlight.js/lib/languages/javascript";
import typescript from "highlight.js/lib/languages/typescript";
import python from "highlight.js/lib/languages/python";
import java from "highlight.js/lib/languages/java";
import css from "highlight.js/lib/languages/css";
import xml from "highlight.js/lib/languages/xml";
import json from "highlight.js/lib/languages/json";
import bash from "highlight.js/lib/languages/bash";
import sql from "highlight.js/lib/languages/sql";
import yaml from "highlight.js/lib/languages/yaml";
import markdown from "highlight.js/lib/languages/markdown";
import go from "highlight.js/lib/languages/go";
import rust from "highlight.js/lib/languages/rust";
import dockerfile from "highlight.js/lib/languages/dockerfile";
import cpp from "highlight.js/lib/languages/cpp"
import CodeBlockLowlight from "@tiptap/extension-code-block-lowlight";

interface TiptapEditorProps {
  initialContent?: JSONContent;
  onSave: (content: JSONContent) => void;
  editable?: boolean;
}

const ydoc = new Doc();

const lowlight = createLowlight();
lowlight.register("javascript", javascript);
lowlight.register("js", javascript);
lowlight.register("typescript", typescript);
lowlight.register("ts", typescript);
lowlight.register("python", python);
lowlight.register("java", java);
lowlight.register("css", css);
lowlight.register("html", xml);
lowlight.register("xml", xml);
lowlight.register("json", json);
lowlight.register("bash", bash);
lowlight.register("shell", bash);
lowlight.register("sql", sql);
lowlight.register("yaml", yaml);
lowlight.register("markdown", markdown);
lowlight.register("go", go);
lowlight.register("rust", rust);
lowlight.register("dockerfile", dockerfile);
lowlight.register("cpp", cpp);

export function TiptapEditor({
  initialContent,
  onSave,
  editable = true
}: TiptapEditorProps) {
  const { pageId } = useParams({ from: "/_protected/workspaces/$workspaceId/pages/$pageId" });

  const { data } = useGetUser();

  const provider = useMemo(() => new HocuspocusProvider({
    name: pageId,
    url: `${(import.meta.env.VITE_API_BASE_URL as string).replace("https", "wss")}/collaboration`,
    document: ydoc,
  }), [pageId, ydoc]);
  const editor = useEditor({
    extensions: [
      Text,
      nodeWithId(Document).extend({
        addAttributes() {
          return {
            ...this.parent?.(),

          }
        }
      }),
      BoldExtension,
      ItalicExtension,
      StrikethroughExtension,
      CodeExtension,
      nodeWithId(TaskList),
      TextStyleKit,
      Subscript,
      Superscript,
      Underline,
      nodeWithId(Blockquote),
      nodeWithId(BulletList.configure({
        HTMLAttributes: {
          class: 'list-disc'
        }
      })),
      nodeWithId(ListItem),
      OrderedList.configure({
        HTMLAttributes: {
          class: 'list-decimal'
        }
      }),
      UndoRedo,
      nodeWithId(TaskItem),
      nodeWithId(CodeBlock.configure({
        tabSize: 2,
        enableTabIndentation: true
      })),
      nodeWithId(HorizontalRule),
      nodeWithId(Paragraph),
      nodeWithId(Heading).configure({
        levels: [1, 2, 3],
      }),
      Collaboration.configure({
        document: ydoc,
        field: "prosemirror",
      }),
      CollaborationCaret.configure({
        provider: provider,
        user: { name: data?.name, color: Math.floor(Math.random() * 16777215).toString(16).padStart(6, '0') }
      }),
      CodeBlockLowlight.configure({
        lowlight
      })
    ],
    editable,
    editorProps: {
      attributes: {
        class: "prose prose-sm sm:prose lg:prose-lg xl:prose-2xl mx-auto focus:outline-none min-h-[500px] p-4",
      },
    },
  });

  const editorState = useEditorState({
    editor,
    selector: ctx => {
      return {
        canUndo: ctx.editor.can().chain().undo().run(),
        canRedo: ctx.editor.can().chain().redo().run(),
      }
    },
  })

  // Sync initial content if it changes (e.g. loaded from server)
  useEffect(() => {
    if (editor && initialContent) {
      if (editor.isEmpty) {
        editor.commands.setContent(initialContent);
      }
    }
  }, [initialContent, editor]);

  if (!editor) {
    return null;
  }

  const handleSave = () => {
    onSave(editor.getJSON());
  };

  return (
    <div className="flex flex-col gap-4 w-full max-w-4xl mx-auto relative">
      {editable && (
        <>
          {/* Bubble Menu for text formatting */}
          {editor && (
            <BubbleMenu editor={editor} resizeDelay={100}
              className="flex bg-background border rounded-md shadow-md p-1 gap-1">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleBold().run()}
                className={editor.isActive("bold") ? "bg-muted" : ""}
              >
                <Bold className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleItalic().run()}
                className={editor.isActive("italic") ? "bg-muted" : ""}
              >
                <Italic className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleStrike().run()}
                className={editor.isActive("strike") ? "bg-muted" : ""}
              >
                <Strikethrough className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleCodeBlock().run()}
                className={editor.isActive("codeBlock") ? "bg-muted" : ""}
              >
                <Code className="h-4 w-4" />
              </Button>
            </BubbleMenu>
          )}

          {/* Floating Menu for block types */}
          {editor && (
            <FloatingMenu editor={editor}
              className="flex bg-background border rounded-md shadow-md p-1 gap-1">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleHeading({ level: 1 }).run()}
                disabled={!editor.isActive("heading", { level: 1 })}
                className={editor.isActive("heading", { level: 1 }) ? "bg-muted" : ""}
              >
                <Heading1 className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleHeading({ level: 2 }).run()}
                disabled={!editor.isActive("heading", { level: 2 })}
                className={editor.isActive("heading", { level: 2 }) ? "bg-muted" : ""}
              >
                <Heading2 className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleBulletList().run()}
                className={editor.isActive("bulletList") ? "bg-muted" : ""}
              >
                <List className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleOrderedList().run()}
                className={editor.isActive("orderedList") ? "bg-muted" : ""}
              >
                <ListOrdered className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleTaskList().run()}
                className={editor.isActive("taskList") ? "bg-muted" : ""}
              >
                <ListTodo className="h-4 w-4" />
              </Button>
            </FloatingMenu>
          )}

          {/* Main Toolbar (Sticky) */}
          <div
            className="flex flex-wrap gap-2 border-b pb-4 sticky top-0 bg-background z-10 items-center">
            <div className="flex gap-1">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleBold().run()}
                // disabled={!editor.isActive("bold")}
                className={editor.isActive("bold") ? "bg-muted" : ""}
              >
                <Bold className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleItalic().run()}
                // disabled={!editor.isActive("italic")}
                className={editor.isActive("italic") ? "bg-muted" : ""}
              >
                <Italic className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleStrike().run()}
                // disabled={!editor.isActive("strike")}
                className={editor.isActive("strike") ? "bg-muted" : ""}
              >
                <Strikethrough className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleUnderline().run()}
                // disabled={!editor.isActive("underline")}
                className={editor.isActive("underline") ? "bg-muted" : ""}
              >
                <UnderlineIcon className="h-4 w-4" />
              </Button>
            </div>
            <div className="w-px h-6 bg-border mx-1" />
            <div className="flex gap-1">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleHeading({ level: 1 }).run()}
                // disabled={!editor.isActive("heading", {level: 1})}
                className={editor.isActive("heading", { level: 1 }) ? "bg-muted" : ""}
              >
                <Heading1 className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleHeading({ level: 2 }).run()}
                // disabled={!editor.isActive("heading", {level: 2})}
                className={editor.isActive("heading", { level: 2 }) ? "bg-muted" : ""}
              >
                <Heading2 className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleHeading({ level: 3 }).run()}
                // disabled={!editor.isActive("heading", {level: 3})}
                className={editor.isActive("heading", { level: 3 }) ? "bg-muted" : ""}
              >
                <Heading3 className="h-4 w-4" />
              </Button>
            </div>
            <div className="w-px h-6 bg-border mx-1" />
            <div className="flex gap-1">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleBulletList().run()}
                className={editor.isActive("bulletList") ? "bg-muted" : ""}
              >
                <List className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleOrderedList().run()}
                className={editor.isActive("orderedList") ? "bg-muted" : ""}
              >
                <ListOrdered className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleTaskList().run()}
                className={editor.isActive("taskList") ? "bg-muted" : ""}
              >
                <ListTodo className="h-4 w-4" />
              </Button>
            </div>
            <div className="w-px h-6 bg-border mx-1" />
            <div className="flex gap-1">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().toggleBlockquote().run()}
                // disabled={!editor.isActive("blockquote")}
                className={editor.isActive("blockquote") ? "bg-muted" : ""}
              >
                <Quote className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().setHorizontalRule().run()}
              >
                <Minus className="h-4 w-4" />
              </Button>
            </div>
            <div className="w-px h-6 bg-border mx-1" />
            <div className="flex gap-1">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().undo().run()}
                disabled={!editorState.canUndo}
                className={editorState.canUndo ? "bg-muted" : ""}
              >
                <Undo className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => editor.chain().focus().redo().run()}
                disabled={!editorState.canRedo}
                className={editorState.canRedo ? "bg-muted" : ""}
              >
                <Redo className="h-4 w-4" />
              </Button>
            </div>
            <div className="flex-1" />
            <Button onClick={handleSave} size="sm">Save
              Changes</Button>
          </div>
        </>
      )}
      <div className="border rounded-md min-h-[500px] bg-card">
        <EditorContent editor={editor} />
      </div>
    </div>
  );
}
